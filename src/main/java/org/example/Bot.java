package org.example;

import org.example.utils.ImageUtils;
import org.example.utils.RgbMaster;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.awt.image.BufferedImage;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

        @Override
        public String getBotUsername() {

            return "bedniy_starboy_98bot";
        }

        @Override
        public String getBotToken() {

            return "6488339068:AAFfxS6fBdc2rwhkiEFzOVOxy644qIAN6oE";
        }

        @Override
        public void onUpdateReceived(Update update) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            try {
                ArrayList<String> photoPaths = new ArrayList<>(PhotoMassageUtils.savePhotos(getFilesByMessage(message), getBotToken()));
                for (String path : photoPaths) {
                    processingImage(path);
                    execute(preparePhotoMessage(path, chatId));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void processingImage(String fileName) throws Exception {
            final BufferedImage image = ImageUtils.getImage(fileName);
            final RgbMaster rgbMaster = new RgbMaster(image);
            rgbMaster.changeImage(FilterOperatons::grayScale);
            ImageUtils.saveImage(rgbMaster.getImage(), fileName);
        }

        private SendPhoto preparePhotoMessage(String localPath, String chatId) {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setReplyMarkup(getKeyboard(FilterOperatons.class));
            sendPhoto.setChatId(chatId);
            InputFile newFile = new InputFile();
            newFile.setMedia(new File(localPath));
            sendPhoto.setPhoto(newFile);
            return sendPhoto;
        }

        private List<File> getFilesByMessage(Message message) {
            List<PhotoSize> photoSizes = message.getPhoto();
            if (photoSizes == null) return new ArrayList<>();
            ArrayList<org.telegram.telegrambots.meta.api.objects.File> files = new ArrayList<>();
            for (PhotoSize photoSize : photoSizes) {
                final String fileId = photoSize.getFileId();
                try {
                    files.add(sendApiMethod(new GetFile(fileId)));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            return files;
        }

        private ReplyKeyboardMarkup getKeyboard(Class someClass) {
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
            Method[] methods = someClass.getMethods();
            int columnCount = 3;
            int rowsCount = methods.length / columnCount + ((methods.length % columnCount == 0) ? 0 : 1);
            for (int rowIndex = 0; rowIndex < rowsCount; rowIndex++) {
                KeyboardRow row = new KeyboardRow();
                for (int columIndex = 0; columIndex < columnCount; columIndex++) {
                    int index = rowIndex * columnCount + columIndex;
                    if (index >= methods.length) continue;
                    Method method = methods[rowIndex * columnCount + columIndex];
                    KeyboardButton keyboardButton = new KeyboardButton(method.getName());
                    KeyboardButton KeyboardButton;
                    row.add(keyboardButton);
                }
                keyboardRows.add(row);
            }
            replyKeyboardMarkup.setKeyboard(keyboardRows);
            replyKeyboardMarkup.setOneTimeKeyboard(true);
            return replyKeyboardMarkup;
        }
    }

