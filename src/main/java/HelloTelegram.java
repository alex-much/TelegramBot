import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.apache.commons.lang3.StringUtils;


import java.util.ArrayList;

public class HelloTelegram {

    public static void main(String[] args) {
        TelegramBot bot = new TelegramBot("Token");

        bot.setUpdatesListener(updates -> {
            updates.forEach(upd -> {
                try {
                    System.out.println(upd);
                    long chatId = upd.message().chat().id();
                    String senderFirstname = upd.message().from().firstName();
                    String incomeMessage = upd.message().text();
                    System.out.println(incomeMessage);
                    String replaceMessage = incomeMessage;
                    ArrayList<String> censorWords = new ArrayList<>();
                    censorWords.add("баран");
                    censorWords.add("лох");
                    censorWords.add("редиска");
                    for (String censorWord : censorWords) {
                        replaceMessage = StringUtils.replaceIgnoreCase(replaceMessage, censorWord, "***");
                    }
                    String message = replaceMessage + ", " + senderFirstname;
                    SendMessage request = new SendMessage(chatId, message);
                    bot.execute(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

    }
}
