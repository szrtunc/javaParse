import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            String json = new String(Files.readString(Paths.get("C:\\Users\\geovi\\Masaüstü\\Projeler\\javaParse\\src\\main\\resources\\data.json")));
            ObjectMapper mapper=new ObjectMapper();


            List<JsonData> jsonDataList = mapper.readValue(json, new TypeReference<List<JsonData>>() {});

            Path outputPath = Paths.get("C:\\Users\\geovi\\Masaüstü\\Projeler\\javaParse\\src\\main\\resources\\output.csv");
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath.toString(), StandardCharsets.UTF_8));
            writer.write("id\tlat\tlng\tpin\ttitle\ttype\taddress\temail\tphone\n");
            for (JsonData jsonData : jsonDataList) {
                Map map=x(jsonData.htmlContent);
                String rowData="";
                rowData=rowData+jsonData.id.toString()+"\t";
                rowData=rowData+jsonData.lat+"\t";
                rowData=rowData+jsonData.lng+"\t";
                rowData=rowData+jsonData.pin+"\t";

                rowData=rowData+map.get("title")+"\t";
                rowData=rowData+map.get("type")+"\t";
                rowData=rowData+map.get("address")+"\t";
                rowData=rowData+map.get("email")+"\t";
                rowData=rowData+map.get("phone")+"\n";
                writer.append(rowData);
            }
            writer.flush();
            writer.close();

        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Map x(String html){

        Document doc = Jsoup.parse(html);
        // Örnek: title ve address bilgilerini almak
        String title = doc.select("div.title").text();
        String type = doc.select("span.mb-2").eq(0).text();
        String address = doc.select("span.mb-2").eq(1).text();
        String email= doc.selectXpath("//ul[@class='mb-2']/li/a[@aria-label]").eq(1).text();
        String phone= doc.selectXpath("//ul[@class='mb-2']/li/a[@aria-label]").eq(0).text();

        Map map=new HashMap<>();
        map.put("title",title);
        map.put("type",type);
        map.put("address",address);
        map.put("email",email);
        map.put("phone",phone);
        return map;
    }
}
