package fr.sarainfras.caillou15.app;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

public class Utils {
    private Utils() {}
    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }

    public static Image downloadImage(String URL) {
        java.net.URL url;
        try {
            url = new URL(URL);
            return ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] addElementAtFirstinStringArray(String[] array) {
        int n = array.length;
        String[] newArr = new String[n+1];
        String value = " ";
        for(int i = 1; i<n+1; i++) {
            newArr[i] = array[i-1];
        }
        newArr[0] = value;
        return newArr;
    }

    public static void GetFilesFromWikimedia(String url) {
        String[] url_arr = {url};
        GetFilesFromWikimedia(url_arr);
    }

    public static void GetFilesFromWikimedia(String[] fileNames) {
        //Get HTML request with all file names
        String url = "https://commons.wikimedia.org/w/api.php?action=query&format=xml" +
                "&prop=imageinfo&iiprop=url&titles=File:" + String.join("|File:", fileNames);
        /*
        using (var webResponse = (HttpWebResponse)WebRequest.Create(url).GetResponse())
        {
            using (var reader = new StreamReader(webResponse.GetResponseStream()))
            {
                var response = reader.ReadToEnd();

                //Get all file url links by parsing the XML response
                var links = XElement.Parse(response).Descendants("ii")
                        .Select(x => x.Attribute("url").Value);
                foreach (var link in links)
                {
                    //Save the current file on the disk
                    using (var client = new WebClient())
                    {
                        var fileName = link.Substring(link.LastIndexOf("/") + 1);
                        client.DownloadFile(link, fileName);
                    }
                }
            }
        }*/
    }

    public static String monoLineText(String[] multiLineText) {
        String result = "";
        for (int i = 0; i < multiLineText.length; i++) {
            result += multiLineText[i];
            if (i != multiLineText.length - 1) result += "\n";
        }
        return result;
    }

}
