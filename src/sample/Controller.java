package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    TextField url;
    @FXML
    Button open;
    @FXML
    TextArea editor;
    @FXML
    Button excute;
    @FXML
    Button reload;
    @FXML
    TextArea console;

    @FXML
    WebView webView;


    public void loadUrl(){
        excute.setDisable(true);
        final WebEngine webEngine = webView.getEngine();
        String text = url.getText();
        webEngine.load(normalize(text));
        webEngine.documentProperty().addListener(new ChangeListener<Document>() {
            @Override public void changed(ObservableValue<? extends Document> observableValue, Document document, Document newDoc) {
                if (newDoc != null) {
                    webEngine.documentProperty().removeListener(this);
                    excute.setDisable(false);
                }
            }
        });
    }
    public void load(ActionEvent event){
        try {
            loadUrl();
        } catch (Exception e) {
            printError(e);
        }
    }
    public void reload(ActionEvent event){
        try {
            final WebEngine webEngine = webView.getEngine();
            webEngine.reload();
        } catch (Exception e) {
            printError(e);
        }
    }


    public void onEnterKey(javafx.scene.input.KeyEvent event){
        if (event.getCode() == KeyCode.ENTER) {
            loadUrl();
        }
    }

    public void excute(ActionEvent event){
        try {
            console.setText("");
            final WebEngine webEngine = webView.getEngine();
            String script = editor.getText();
            webEngine.executeScript(script);

        } catch (Exception e) {
            printError(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final WebEngine webEngine = webView.getEngine();
        webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
            @Override
            public void handle(WebEvent<String> event) {
                console.appendText(event.getData().concat("\n"));
            }
        });
        loadUrl();
        editor.setText("document.getElementById('kw').value='soso';\n" +
                "document.getElementById('form').submit();");
    }

    private void printError(Throwable throwable){
        console.appendText(throwable.getLocalizedMessage());
        console.appendText("\n");
    }

    private String normalize(String url){
        if(url.matches("\\w+://.*")){
            return url;
        }else {
            return "http://"+url;
        }
    }
}
