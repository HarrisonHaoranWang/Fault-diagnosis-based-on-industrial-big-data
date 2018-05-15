/* *********************
 * Author   :   HustWolf --- 张照博

 * Time     :   2018.1-2018.5

 * Address  :   HUST

 * Version  :   1.0

 * 从数据库中读取符合SVM格式的数据
 ********************* */
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;

public class SVMReadData {
    private Object[] Name;
    protected Mysql_Connect mysql=new Mysql_Connect();
    private static int atoi(String s) {
        return Integer.parseInt(s);
    }
    SVMReadData() {
        Name = new Object[]{"Sensor1","Sensor2","Sensor3","Sensor4", "Load", "category"};
    }

    public String readTrainData() {
        try {
            mysql.Connect();
            Statement statement=mysql.getStatement();
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMaximumFractionDigits(1);
            FileWriter svmTrainData = new FileWriter("svmTrainData.txt");
            int columnCount = Parameter.getTrainNum();
            Object[][] DataTrain;
            DataTrain = new Object[columnCount][Name.length];
            for (int  i = 0;i<columnCount;++i) {
                String getDataQuery = ReadData.getSelectQuery(Name,"gear",i*Parameter.getTrainDistance());
                ResultSet select_ok;
                select_ok = statement.executeQuery(getDataQuery);
                select_ok.next();
                for (int j = 0; j < Name.length; ++j){
//                    DataTrain[i][j]=Float.parseFloat(nf.format(select_ok.getFloat((String) Name[j])));
                    DataTrain[i][j]=select_ok.getFloat((String) Name[j]);

                }
                if((Float)DataTrain[i][DataTrain[0].length-1] == 1.0)
                    svmTrainData.write("1 ");
                else
                    svmTrainData.write("0 ");
                for (int j = 0; j<DataTrain[0].length-1; ++j ){
                    svmTrainData.write(j+":"+DataTrain[i][j]+" ");
                }
                svmTrainData.write("\n");
            }
            statement.close();
            mysql.Dis_Connect();
            svmTrainData.close();
            return "svmTrainData.txt";
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "svmTrainData.txt";
    }

    public String readTestData() {
        try {
            mysql.Connect();
            Statement statement=mysql.getStatement();
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMaximumFractionDigits(1);
            FileWriter svmTestData = new FileWriter("svmTestData.txt");
            int columnCount = Parameter.getTestNum();
            Object[][] DataTest;
            DataTest = new Object[columnCount][Name.length];
            for (int  i = 0;i<columnCount;++i) {
                String getDataQuery = ReadData.getSelectQuery(Name,"gear",i*Parameter.getTestDistance()+1);
                ResultSet select_ok;
                select_ok = statement.executeQuery(getDataQuery);
                select_ok.next();
                for (int j = 0; j < Name.length; ++j){
//                    DataTest[i][j]=Float.parseFloat(nf.format(select_ok.getFloat((String) Name[j])));
                    DataTest[i][j]=select_ok.getFloat((String) Name[j]);
                }
                if((Float)DataTest[i][DataTest[0].length-1] == 1)
                    svmTestData.write("1 ");
                else
                    svmTestData.write("0 ");
                for (int j = 0; j<DataTest[0].length-1; ++j ){
                    svmTestData.write(j+":"+DataTest[i][j]+" ");
                }
                svmTestData.write("\n");
            }
            statement.close();
            mysql.Dis_Connect();
            svmTestData.close();
            return "svmTestData.txt";
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "svmTestData.txt";
    }
}
