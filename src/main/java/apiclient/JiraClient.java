package apiclient;


import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.BasicIssueType;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.IssueType;
import com.atlassian.jira.rest.client.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.domain.input.TransitionInput;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import javafx.animation.TransitionBuilder;

import java.net.URI;
import java.net.URISyntaxException;


public class JiraClient {
    private static JiraClient instance = null;
    URI jiraServerUri = new URI("http://192.168.43.57:32768");
    final AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
    final JiraRestClient restClient = factory.createWithBasicHttpAuthentication(jiraServerUri, "botuser", "botuser");

    public JiraClient() throws URISyntaxException {
    }

    public static JiraClient getInstance() {
        if(instance == null) {
            try {
                instance = new JiraClient();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public static void main(String[] args) throws Exception {
//        BasicIssue issue = JiraClient.getInstance().createJira("Test");
        JiraClient.getInstance().closeJira("FXOEV-5");
        System.out.printf("");
    }

    public BasicIssue createJira(String summary) {
        try {
//            final Issue issue = restClient.getIssueClient().getIssue("FXOEV-1").claim();
            IssueInputBuilder issueBuilder = new IssueInputBuilder("FXOEV", 10001L, summary);
            return restClient.getIssueClient().createIssue(issueBuilder.build()).claim();
//            System.out.printf(issue.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeJira(String jiraId) {
        Issue issue = restClient.getIssueClient().getIssue(jiraId).claim();
        Iterable it = restClient.getIssueClient().getTransitions(issue).claim();
        restClient.getIssueClient().transition(issue, new TransitionInput(41)).claim();
    }
}
