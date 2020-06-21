package fr.craftyourmind.launcher.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.craftyourmind.launcher.MainAux;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;

public class Auth {
    public File usernameFile = new File(Launcher.LC_DIR, "username.txt");

    public File passwordFile = new File(Launcher.LC_DIR, "password.txt");

    public Auth(String username, String password) throws Exception {
        String body = "{" +
                "\"username\":\"" + username + "\","
                + "\"password\":\"" + password + "\"}";

        StringEntity entity = new StringEntity(body,
                ContentType.APPLICATION_JSON);
        try {

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost verifyRequest = new HttpPost("https://craftyourmind.fr/api/auth");
            verifyRequest.setEntity(entity);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            String response = httpClient.execute(verifyRequest, responseHandler);
            JsonObject respJson = new JsonParser().parse(response).getAsJsonObject();
            respJson.get("");

            if (respJson.get("valid").getAsBoolean()) {
                EncryptionUtil encryptionUtil = new EncryptionUtil();
                if (MainAux.getInstance().getConfig().getBoolean("usernameSave")) {
                    BufferedWriter writerPass = new BufferedWriter(new FileWriter(this.usernameFile));
                    writerPass.write(username);
                    writerPass.close();
                }
                if (MainAux.getInstance().getConfig().getBoolean("passwordSave")) {
                    BufferedWriter writerPass = new BufferedWriter(new FileWriter(this.passwordFile));
                    writerPass.write(encryptionUtil.encrypt("cym%$$", password));
                    writerPass.close();
                }
                if (respJson.get("beta").getAsBoolean()) {
                    MainAux.getInstance().username = username;
                    MainAux.getInstance().token = respJson.get("token").getAsString();
                    MainAux.getInstance().uuid = respJson.get("uuid").getAsString();
                    Stage beta = new Stage();
                    beta.setTitle("Beta");
                    beta.setScene(new Scene((new FXMLLoader(getClass().getResource("/fxml/Beta.fxml"))).load(), 450, 170));
                    beta.initStyle(StageStyle.UNDECORATED);
                    beta.show();
                } else {
                    Launcher.update(false, username, respJson.get("token").getAsString(), respJson.get("uuid").getAsString());
                }
            } else {
                MainAux.getInstance().cancel();
            }
        } catch (HttpHostConnectException e) {
            MainAux.getInstance().cancel();
            e.printStackTrace();
        }
    }
}