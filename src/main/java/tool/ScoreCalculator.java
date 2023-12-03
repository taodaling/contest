package tool;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import template.utils.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;

public class ScoreCalculator {
    private static String getCellValue(Row row, int col) {
        var cell = row.getCell(col);
        if(cell == null) {
            return "";
        }
        return cell.toString();
    }

    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream("D:\\source\\life\\大学成绩.xls");
        var wb = WorkbookFactory.create(fis);
        var sheet = wb.getSheetAt(0);
        double totalGrade = 0;
        double sum = 0;
        double score100Sum = 0;
        double score100Total = 0;
        for(int i = 0; ; i++) {
            var row = sheet.getRow(i);
            if(row == null || row.getCell(0) == null) break;
            var gradeStr = row.getCell(0).toString();
            if(StringUtils.isNullOrEmpty(gradeStr)) {
                break;
            }

            double grade = Double.parseDouble(gradeStr);
            if(row.getCell(2) == null || StringUtils.isNullOrEmpty(row.getCell(2).toString())) {
                score100Sum += Double.parseDouble(getCellValue(row, 1)) ;
                score100Total += 1;
            }
            sum += grade * getScore(grade, getCellValue(row, 1), getCellValue(row, 2));
            totalGrade += grade;


            System.out.println(i + "-" + getCellValue(row, 0) + "-" + getCellValue(row, 1) + "-" + getCellValue(row, 2) + "-" + getScore(grade, getCellValue(row, 1), getCellValue(row, 2)));

        }
        System.out.println(sum);
        System.out.println(totalGrade);
        System.out.println(sum / totalGrade);
        System.out.println(score100Sum / score100Total);
    }

    public static double getScore(double grade, String score, String desc) {
        if(StringUtils.isNullOrEmpty(desc)) {
            double val = Double.parseDouble(score);
            if(val >= 95) {
                return 5;
            }
            if(val >= 90) {
                return 4.5 + (val - 90) / 10;
            }
            if(val >= 80) {
                return 3.5 + (val - 80) / 10;
            }
            if(val >= 70) {
                return 2.5 + (val - 70) / 10;
            }
            if(val >= 60) {
                return 1.5 + (val - 60) / 10;
            }
            return 0;
        } else {

            switch (desc) {
                case "优秀": return 5;
                case "良好": return 4;
                case "中等": return 3;
                case "及格":
                case "合格":
                    return 2;
                default: return 0;
            }
        }
    }
}
