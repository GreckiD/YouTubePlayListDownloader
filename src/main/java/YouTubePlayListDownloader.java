import com.google.api.services.youtube.YouTube;
import com.jayway.jsonpath.JsonPath;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

public class YouTubePlayListDownloader {

    public static void main(String[] args) throws GeneralSecurityException, IOException {
        Authorization authorization = new Authorization();
        YouTube youtubeService = authorization.getService();
        YouTube.PlaylistItems.List request = youtubeService.playlistItems().list("snippet,contentDetails");
        Object PlayListDetails = request.setPlaylistId("PL99oCUNFrGU6kJaOS7W5FW3gElpwNviRO").setMaxResults(100L).execute();
        ArrayList<String> videoIdList = JsonPath.parse(PlayListDetails).read("$.items[*].snippet.resourceId.videoId");
        ArrayList<String> videoTitleList = JsonPath.parse(PlayListDetails).read("$.items[*].snippet.title");
        int videoCount = JsonPath.parse(PlayListDetails).read("$.pageInfo.totalResults");
        VideoConverter videoConverter = new VideoConverter();
        for (int i = 0; i < videoCount; i++) {
            videoConverter.downloadMP3File(videoIdList.get(i), videoTitleList.get(i));
        }
    }
}