package com.mr;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class GitHubRepoFetcher {

    public static void main(String[] args) {
        String username = "microsoft";
        String token = "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"; // Replace with your actual token
        String outputFilePath = "repo_details_ms.json"; // Change the path as needed

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            int page = 1;
            int perPage = 100; // Number of repositories per page
            JSONArray allRepos = new JSONArray();

            while (true) {
                HttpGet request = new HttpGet("https://api.github.com/users/" + username + "/repos?page=" + page + "&per_page=" + perPage);
                request.addHeader("Authorization", "Bearer " + token);

                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    HttpEntity entity = response.getEntity();
                    String responseBody = EntityUtils.toString(entity);

                    JSONArray repos = new JSONArray(responseBody);

                    if (repos.length() == 0) {
                        break; // No more repositories
                    }

                    allRepos.put(repos);

                    page++;
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
                for (int i = 0; i < allRepos.length(); i++) {
                    JSONArray repos = allRepos.getJSONArray(i);

                    for (int j = 0; j < repos.length(); j++) {
                        JSONObject repo = repos.getJSONObject(j);
                        String repoName = repo.getString("name");

                        // Fetch detailed repository information
                        HttpGet repoRequest = new HttpGet("https://api.github.com/repos/" + username + "/" + repoName);
                        repoRequest.addHeader("Authorization", "Bearer " + token);

                        try (CloseableHttpResponse repoResponse = httpClient.execute(repoRequest)) {
                            HttpEntity repoEntity = repoResponse.getEntity();
                            String repoResponseBody = EntityUtils.toString(repoEntity);

                            JSONObject detailedRepo = new JSONObject(repoResponseBody);
                            String primaryLanguage = detailedRepo.optString("language", "Unknown");

                            JSONObject repoDetails = new JSONObject();
                            repoDetails.put("Repository", repoName);
                            repoDetails.put("Language", primaryLanguage);

                            writer.write(repoDetails.toString());
                            writer.newLine();
                        }
                    }
                }
            }

            System.out.println("Repository details saved to " + outputFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
