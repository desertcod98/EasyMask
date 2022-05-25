package it.volta.ts.easymask.networking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class UploadManager {
    private static final String NEWLINE = "\r\n";

    private final String boundary;

    private HttpURLConnection cn;

    private String charset;

    private PrintWriter printWriter;

    private OutputStream outStream;

	public UploadManager(String requestURL, String charset) throws IOException {

            //crea un delimitatore univoco basato sul timestamp corrente
            boundary = "===" + System.currentTimeMillis() + "===";

            this.charset = charset;

            URL url = new URL(requestURL);

            cn = (HttpURLConnection) url.openConnection();
            cn.setUseCaches(false);
            //indichiamo il metodo POST
            cn.setDoOutput(true);
            cn.setDoInput(true);
            cn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            outStream = cn.getOutputStream();
            printWriter = new PrintWriter(new OutputStreamWriter(outStream, charset), true);
        }

        public void setHeaderField(String name, String value) {
            printWriter.append(name + ": " + value).append(NEWLINE);
            printWriter.flush();
        }

        public void setFilePart(String fieldName, File uploadFile) throws IOException {
            String fileName = uploadFile.getName();
            printWriter.append("--" + boundary).append(NEWLINE);
            printWriter.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"").append(NEWLINE);
            printWriter.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(NEWLINE);
            printWriter.append("Content-Transfer-Encoding: binary").append(NEWLINE);
            printWriter.append(NEWLINE);
            printWriter.flush();

            FileInputStream inputStream = new FileInputStream(uploadFile);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            outStream.flush();
            inputStream.close();

            printWriter.append(NEWLINE);
            printWriter.flush();
        }

        public void setFormField(String name, String value) {
            printWriter.append("--" + boundary).append(NEWLINE);
            printWriter.append("Content-Disposition: form-data; name=\"" + name + "\"");
            printWriter.append("Content-Type: text/plain; charset=" + charset).append(NEWLINE);
            printWriter.append(NEWLINE);
            printWriter.append(value).append(NEWLINE);
            printWriter.flush();
        }

        public String getResponse() throws IOException {
            StringBuffer response = new StringBuffer();

            printWriter.append(NEWLINE).flush();
            printWriter.append("--" + boundary + "--").append(NEWLINE);
            printWriter.close();

            //verifichiamo la risposta del server
            int status = cn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(cn.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                cn.disconnect();
            } else {
                throw new IOException("Server status: " + status);
            }

            return response.toString();
        }
    }

