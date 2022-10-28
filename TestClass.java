import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.*;

public class TestClass {
    public static void main(String[]args) throws IOException {
        Scanner sc= new Scanner(System.in);
        System.out.print("Keyword: ");
        String key = sc.nextLine();
        String urL = "https://unsplash.com/s/photos/"+key;
        String content= getContentFromLink(urL);
        ArrayList<String> imgLinks= getImageLinks(content);
        int totalImage= imgLinks.size();
        System.out.println(totalImage + " images had been found.\n Start download: ");
        if(totalImage>0){
            createFolder(key);
            for (int i=0;i<totalImage;i++){
                downloader(imgLinks.get(i), String.valueOf(i+1),key);
            }
            System.out.print("\n");
            System.out.println("FINISH, total: "+totalImage+" images, store at: "+ "downloaded-image/"+key);
        }


    }

    //GET CONTENT FROM LINK AND CONVERT TO A STRING
    private static String getContentFromLink (String link) throws IOException{
        URL url= new URL(link);
        Scanner sc = new Scanner(new InputStreamReader(url.openStream()));
        sc.useDelimiter("\\\\Z");
        String content= sc.next();
        sc.close();
        content=content.replaceAll("\\\\R","");
        return content;
    }

    //HANDLE CONTENT DATA AND GET IMAGE LINKS
    private static ArrayList<String> getImageLinks(String s){
        ArrayList<String> links = new ArrayList<>();
        String key1 ="<div class=\"MorZF\"><div class=\"VQW0y Jl9NH\"><img class=\"YVj9w\"";
        String key2="src=\"";
        String key3="\"";
        while(s.contains(key1)){
            s=s.substring(s.indexOf(key1));
            s=s.substring(s.indexOf(key2));
            s=s.substring(key2.length());
            int k = s.indexOf(key3);
            String ans = s.substring(0,k);
            links.add(ans);
            s=s.substring(k);
        }
        return links;
    }

    //DOWNLOAD AND SAVE IMAGE FROM LINK
    private static void downloader(String url, String name, String folderName){
        BufferedImage image = null;
        createFolder(folderName);
        try{
            URL u = new URL(url);
            image = ImageIO.read(u);
            ImageIO.write(image,"jpeg", new File("downloaded-image/"+folderName+"/"+name+".jpeg"));
            System.out.print(name+" ");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //CREATE A NEW FOLDER TO STORE THE IMAGES TO BE DOWNLOADED
    private static void createFolder(String folderName){
        File f = new File("downloaded-image/"+folderName);
        if(!f.exists()){
            f.mkdir();
        }
    }

}
