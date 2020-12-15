package models;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import models.quiz.Question;
import models.quiz.Quiz;
import wrappers.WrappedGoogleSheet;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class GoogleDocks {
    private static final String GOOGLE_SHEETS_API_LINK = System.getenv("GOOGLE_SHEETS_API_LINK");
    private static final String SPREADSHEET_ID = System.getenv("SPREADSHEET_ID");

    public static Boolean uploadQuizResult(String username, int testNumber, List<Boolean> userAnswers) {
        int countPoints = getCountRightAnswers(userAnswers);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(GOOGLE_SHEETS_API_LINK);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("method", "updateRating"));
            nvps.add(new BasicNameValuePair("username", username));
            nvps.add(new BasicNameValuePair("testNumber", String.valueOf(testNumber)));
            nvps.add(new BasicNameValuePair("result", String.valueOf(countPoints)));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse response = httpclient.execute(httpPost);
            return response.getStatusLine().getStatusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    private static int getCountRightAnswers(List<Boolean> userAnswers) {
        int countRightAnswers = 0;
        for (Boolean isRightAnswer : userAnswers)
            if (isRightAnswer)
                countRightAnswers++;
        return countRightAnswers;
    }

    public static Quiz getQuizByNumber(Integer quizNumber) {
        try {
            Sheets sheets = WrappedGoogleSheet.getSheets();
            String range = quizNumber.toString();
            ValueRange response = sheets.spreadsheets().values()
                    .get(SPREADSHEET_ID, range).execute();

            List<List<Object>> values = response.getValues();
            if (values.size() == 1) return null;
            ArrayList<Question> questions = new ArrayList<>();
            for (int i = 1; i < values.size(); i++) {
                Question question = rowToQuestion(values.get(i));
                questions.add(question);
            }
            return new Quiz(quizNumber, questions);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Rating getRating() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            Rating rating = null;
            HttpGet request = new HttpGet(GOOGLE_SHEETS_API_LINK);
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String jsonStr = EntityUtils.toString(entity);
                Gson gson = new Gson();
                rating = gson.fromJson(jsonStr, Rating.class);
            }
            response.close();
            return rating;
        } catch (IOException | ParseException | JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static List<Boolean> getUserCompletedTests(String username) {
        try {
            Sheets sheets = WrappedGoogleSheet.getSheets();
            String range = "Rating";
            ValueRange response = sheets.spreadsheets().values()
                    .get(SPREADSHEET_ID, range).execute();

            List<List<Object>> values = response.getValues();
            for (List<Object> row : values) {
                if (row.get(0).equals(username))
                    return rowToCompletedTests(row);
            }
            return getAllNonCompletedTests(values.get(0));
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<Boolean> getAllNonCompletedTests(List<Object> row) {
        ArrayList<Boolean> nonCompletedTests = new ArrayList<>();
        for (int i = 1; i < row.size() - 1; i++) {
            nonCompletedTests.add(false);
        }
        return nonCompletedTests;
    }

    private static List<Boolean> rowToCompletedTests(List<Object> row) {
        ArrayList<Boolean> completedTests = new ArrayList<>();
        for (int i = 1; i < row.size() - 1; i++) {
            completedTests.add(row.get(i) != "");
        }
        return completedTests;
    }

    private static Question rowToQuestion(List<Object> row) {
        Integer countColumns = row.size();
        String question = row.get(0).toString();
        String answer = row.get(countColumns - 1).toString();
        ArrayList<String> variants = new ArrayList<>();
        for (int i = 1; i < countColumns - 1; i++)
            variants.add(row.get(i).toString());
        return new Question(question, variants, answer);
    }
}
