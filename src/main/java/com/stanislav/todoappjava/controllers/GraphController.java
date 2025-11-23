package com.stanislav.todoappjava.controllers;

import com.google.gson.Gson;
import com.stanislav.todoappjava.interfaces.INotesService;
import com.stanislav.todoappjava.models.NoteModel;
import com.stanislav.todoappjava.services.NotesService;
import com.stanislav.todoappjava.utils.JsBridge;
import com.stanislav.todoappjava.utils.SceneManager;
import com.stanislav.todoappjava.utils.SessionManager;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GraphController {

    @FXML
    private WebView webView;

    private final INotesService notesService;


    public GraphController() {
        this.notesService = new NotesService();
    }

    @FXML
    @SuppressWarnings("removal")
    private void initialize() {
        WebEngine engine = webView.getEngine();
        String path = Objects.requireNonNull(getClass().getResource("/com/stanislav/todoappjava/js/cytoscape.min.js")).toExternalForm();

        var notes = this.notesService.getUserNotes(SessionManager.getCurrentUser().getId());
        Map<String, Object> graph = this.buildGraph(notes);
        Gson gson = new Gson();
        String graphJson = gson.toJson(graph);
        IO.println(graphJson);

        String html = String.format("""
                <html>
                <head>
                <script src="%s"></script>
                <style>
                html, body, #cy {
                    margin: 0;
                    padding: 0;
                    width: 100vw;
                    height: 100vh;
                    display: block;
                    background-color: #1e1e1e;
                }
                </style>
                </head>
                <body>
                <div id="cy"></div>
                <script>
                const graph = %s;
                alert(JSON.stringify(graph))
                const cy = cytoscape({
                    container: document.getElementById('cy'),
                    elements: graph.nodes.concat(graph.edges),
                  style: [
                                    {
                                      selector: 'node',
                                      style: {
                                        'label': 'data(label)',
                                        'shape': 'ellipse',
                                        'width': 50,
                                        'height': 50,
                                        'background-color': '#1f77b4',
                                        'color': '#fff',
                                        'text-valign': 'center',
                                        'text-halign': 'center',
                                        'text-outline-color': '#1e1e1e',
                                        'text-outline-width': 2
                                      }
                                    },
                                    {
                                      selector: 'edge',
                                      style: {
                                        'line-color': '#999',
                                        'width': 2,
                                        'target-arrow-shape': 'triangle',
                                        'target-arrow-color': '#999',
                                        'curve-style': 'bezier'
                                      }
                                    }
                                  ],
                    layout: {
                        name: 'cose',
                        animate: true,
                        dealEdgeLength: 400
                    }
                });
                
                // Подгоняем размер графа под WebView
                setTimeout(() => {
                    cy.resize();
                    cy.fit();
                }, 100);
                
                </script>
                </body>
                </html>
                """, path, graphJson);

        engine.setOnAlert(event -> System.out.println("JS alert: " + event.getData()));

        engine.getLoadWorker().exceptionProperty().addListener((obs, oldEx, newEx) -> {
            if (newEx != null) newEx.printStackTrace();
        });

        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) engine.executeScript("window");

                window.setMember("java", new JsBridge(this));

                engine.executeScript("""                        
                            cy.on('tap', 'node', evt => {
                                const noteId = evt.target.id();
                               java.openNote(noteId)
                            });
                        """);
            }
        });

        engine.setJavaScriptEnabled(true);
        engine.loadContent(html);

    }

    public void openNoteById(int id) {
        SceneManager.getMainController().openNoteInMain(id);
    }

    private Map<String, Object> buildGraph(List<NoteModel> notes) {
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> edges = new ArrayList<>();

        // Map: название заметки -> NoteModel
        Map<String, NoteModel> titleToNote = notes.stream()
                .collect(Collectors.toMap(NoteModel::getTitle, n -> n));

        // создаем узлы
        for (NoteModel note : notes) {
            nodes.add(Map.of("data", Map.of(
                    "id", String.valueOf(note.getId()),
                    "label", note.getTitle()
            )));
        }

        // создаем связи по #Название
        Pattern pattern = Pattern.compile("#([\\wА-Яа-яЁё0-9_]+)");
        for (NoteModel note : notes) {
            Matcher matcher = pattern.matcher(note.getContent());
            while (matcher.find()) {
                String linkedTitle = matcher.group(1);
                if (titleToNote.containsKey(linkedTitle)) {
                    NoteModel linkedNote = titleToNote.get(linkedTitle); // вот откуда берём linkedNote
                    edges.add(Map.of("data", Map.of(
                            "source", String.valueOf(note.getId()),
                            "target", String.valueOf(linkedNote.getId())
                    )));
                }
            }
        }

        Map<String, Object> graph = new HashMap<>();
        graph.put("nodes", nodes);
        graph.put("edges", edges);
        return graph;
    }
}
