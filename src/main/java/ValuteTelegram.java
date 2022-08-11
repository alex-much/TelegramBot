import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ValuteTelegram {
    public static void main(String[] args) {
        TelegramBot bot = new TelegramBot("Token");

        bot.setUpdatesListener(updates -> {
            updates.forEach(upd -> {
                try {
                    System.out.println(upd);
                    long chatId = upd.message().chat().id();
                    String incomeMessage = upd.message().text();
                    String result = "";
                    if (incomeMessage.equals("/start")) {
                        result = "Привет! Для просмотра курса рубля введите команду /curs" + "\n" +
                        "Для просмотра новостей введите команду /news";
                    } else if ((incomeMessage.equals("/curs"))){
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                        String textDate = dtf.format(LocalDate.now());
                        Document doc = Jsoup.connect("https://cbr.ru/scripts/XML_daily.asp?date_req=" + textDate ).get();
                        Elements valutes = doc.select("Valute");
                        result = "Курс Российского рубля (cbr.ru) на " + textDate + ":" + "\n";
                        for (Element valute : valutes) {
                            result += valute.select("Name").text() + " - " +
                                    valute.select("Value").text() + "\n";
                        }
                    } else if (incomeMessage.equals("/news")) {
                        Document doc = Jsoup.connect("https://lenta.ru/rss").get();
                        Elements news = doc.select("item");
                        result = "";
                        int countNews = 0;
                        for (Element item: news) {
                            String category = item.select("category").text();
                            String title = item.select("title").text();
                            String link = item.select("link").text();
                            String description = item.select("description").text();
                            result += category + "\n" + title +"\n" + link + "\n" + description + "\n\n";
                            countNews++;
                            if (countNews == 5) {break;}
                        }
                    }
                    SendMessage request = new SendMessage(chatId, result);
                    bot.execute(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
