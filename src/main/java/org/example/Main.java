package org.example;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        /*ВБД ПП
        String filePath = "d:\\Users\\vlad\\Documents\\Source\\BOSS_WBD_PP_2008_2013\\"; //Путь к файлу ВБД ПП
        String fileDestPath = "d:\\Users\\vlad\\Documents\\Source\\BOSS_WBD_PP_2008_2013\\ЛС_ВБД_ПП_2008\\"; //Целевой каталог
        String filesourcrName = "ЛС_ВБД_ПП_2008.pdf"; //Имя файла
        String fileTargetName = "ЛС_ВБД_ПП_2008_";*/

        //ВБД
        String filePath ="d:\\Users\\vlad\\Documents\\Source\\BOSS_WBD_2008_2013\\";
        String fileDestPath = "d:\\Users\\vlad\\Documents\\Source\\BOSS_WBD_2008_2013\\ЛС_ВБД_2013\\"; //Целевой каталог
        String filesourcrName = "ЛС_ВБД_2013.pdf"; //Имя файла
        String fileTargetName = "ЛС_ВБД_2013_";



        String extension = ".pdf";
        String delimeter = "Унифицированная форма  № Т - 54а"; //Опорный разделитель на лицевые счета
        int posOfName = 47; //Номер позиции в строек начала имени
        int firstLetter = 0;

        File file = new File(filePath + filesourcrName); //Создаем объект файл
        PDDocument document = PDDocument.load(file); //Создаем обект pdf
        List<Ls> lsList = buildLS(document, delimeter, posOfName, firstLetter); //Построение карты ЛС в разрезе сотрудников
        List<LsGrouped> map = buildLsGrouped(lsList); //Построение карты ЛС в разрезе алфавита
        saving(fileDestPath, fileTargetName, extension, map, document); //Разделение на отдельные файлы

        System.out.println("End");

    }

    //Сохранение в отдельные файлы
    public static void saving(String fileDestPath, String fileTargetName, String extension, List<LsGrouped> map, PDDocument document) throws IOException {

        for(int index = 0; index < map.size(); index++ ){
            int startPage = map.get(index).getStartPos();
            int endPage = map.get(index).getEndPos();
            char letter = map.get(index).getLetter();
            Splitter splitter = new Splitter();
            splitter.setStartPage(startPage);
            splitter.setEndPage(endPage);
            splitter.setSplitAtPage(endPage - startPage + 1);
            List<PDDocument> lst = splitter.split(document);
            PDDocument doc = lst.get(0);
            File destFile = new File(fileDestPath + fileTargetName + letter+ extension);
            doc.save(destFile);
            doc.close();
        }

    }


    //Опредедяем есть ли в строке ключевой разделитель ЛС
    public static boolean hasDelimeter(String string, String delimeter){
        return string.contains(delimeter);
    }


    //Построение карты ЛС в разрезе сотрудников
    public static List<Ls> buildLS(PDDocument document, String delimeter,
                                   int posOfName, int firstLetter) throws IOException {
        PDFTextStripper pdfTextStripper = new PDFTextStripper(); //Создаем объект для извлечения текста из pdf
        List<Ls> lsList = new ArrayList<>(); //список для хранения данных о ЛС
        for(int i = 1; i <= document.getNumberOfPages(); i++) { //Перебираем все страницы
            pdfTextStripper.setStartPage(i);
            pdfTextStripper.setEndPage(i);
            String string = pdfTextStripper.getText(document);

            if(hasDelimeter(string, delimeter)){
                List<String> list = new ArrayList<>(Arrays.asList(string.split("\r\n")));
                String name = list.get(posOfName);
                lsList.add(new Ls(Character.toUpperCase(name.charAt(firstLetter)), i, name));
            }
        }

        //Дополнение карты ЛС номерами страниц окончания ЛС
        for(int i = 1; i < lsList.size(); i++){
            lsList.get(i - 1).setEndLS(lsList.get(i).getStartLS()-1);
        }
        lsList.get(lsList.size()-1).setEndLS(document.getNumberOfPages());

        return lsList;
    }


    //Построение карты ЛС в разрезе алфавита
    public static List<LsGrouped> buildLsGrouped(List<Ls> lsList){
        List<LsGrouped> map = new ArrayList<>();
        char letter = lsList.get(0).getLetter(); //временное хранение первой буквы списка
        map.add(new LsGrouped(letter, lsList.get(0).getStartLS(), lsList.get(0).getEndLS()));
        int pointer = map.size() - 1;

        for(int i = 1; i < lsList.size(); i++){
            if(lsList.get(i).getLetter() != letter){
                letter = lsList.get(i).getLetter();
                map.add(new LsGrouped(letter, lsList.get(i).getStartLS(), lsList.get(i).getEndLS()));
                pointer = map.size() - 1;
            }
            else{
                map.get(pointer).setEndPos(lsList.get(i).getEndLS());
            }
            //System.out.println(i  + " " + pointer + " " + letter);
        }
        map.get(map.size() - 1).setEndPos(lsList.get(lsList.size()-1).getEndLS());

        return map;
    }

}