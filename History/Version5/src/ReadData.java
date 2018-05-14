
/* *********************
 * Author   :   HustWolf --- 张照博

 * Time     :   2018.1-2018.5

 * Address  :   HUST

 * Version  :   6.0

 * 从数据库读取数据，并且从ReadData这个函数传出去！5.0加了训练集和验证集的划分
 *
 * 第六代提供了离散化方式并且增加了属性划分区间这一变量。
 ********************* */


/* *******************
* 这是从数据库或者是文本文件读取数据的时候用的
* 其实我觉得如果可以每一次读一条数据，然后处理一条会比较好
* 但是算了，数据量不大的话，这个样子也不会增加太多时间的！
******************* */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ReadData {
    private Object[] Name;
    private Mysql_Connect mysql=new Mysql_Connect();
    ArrayList<List<Float>> range;

    ReadData() {
        Name = new Object[]{"Sensor1","Sensor2","Sensor3","Sensor4", "Load", "category"};
    }

    public static String getSelectQuery(Object[] Name,String table,int id){
        String select = "SELECT  ";
        for (int i=0;i<Name.length-1;++i){
            select += ("`"+(Name[i]+"`"+","));
        }
        select += ("`"+Name[Name.length-1]+"`");
        select += " from " + table + " where id = "+id;
//        System.out.println(select);
        return select;
    }

    public Object[][] readTrainData() {
        try {
            mysql.Connect();
            Statement statement=mysql.getStatement();
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMaximumFractionDigits(1);
            int columnCount = Parameter.getTrainNum();
            float [][] dataToTrain;
            dataToTrain = new float[columnCount][Name.length];
            for (int  i = 0;i<columnCount;++i) {
                String getDataQuery = getSelectQuery(Name,"gear",i*Parameter.getTrainDistance());
                ResultSet select_ok;
                select_ok = statement.executeQuery(getDataQuery);
                select_ok.next();
                for (int j = 0; j < Name.length; ++j){
                    dataToTrain[i][j]=Float.parseFloat(nf.format(select_ok.getFloat((String) Name[j])));
                }
            }
            range = Parameter.EADC(dataToTrain);
            Object[][] re = new Object[columnCount][Name.length];
            for (int valueindex = 0;valueindex<Name.length-1;++valueindex) {
                for (int i = 0; i < dataToTrain.length; ++i) {
                    for (int x = 0; x < range.get(valueindex).size(); ++x) {
                        if (dataToTrain[i][valueindex] > range.get(valueindex).get(x) && dataToTrain[i][valueindex] <= range.get(valueindex).get(x+1)) {
                            re[i][valueindex] = ("|"+range.get(valueindex).get(x) + "<X≤" + range.get(valueindex).get(x + 1)+"|");
                            break;
                        }
                    }
                }
            }
            for (int i = 0; i < dataToTrain.length; ++i) {
                re[i][Name.length-1] = dataToTrain[i][Name.length-1];
            }
            statement.close();
            mysql.Dis_Connect();
            return re;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Object[1][1];
    }

    public Object[][] readTestData() {
        try {
            mysql.Connect();
            Statement statement=mysql.getStatement();
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMaximumFractionDigits(1);
            int columnCount = Parameter.getTestNum();
            float[][] dataToTest;
            dataToTest = new float[columnCount][Name.length];
            for (int  i = 0;i<columnCount;++i) {
                String getDataQuery = getSelectQuery(Name,"gear",i*Parameter.getTestDistance()+1);
                ResultSet select_ok;
                select_ok = statement.executeQuery(getDataQuery);
                select_ok.next();
                for (int j = 0; j < Name.length; ++j){
                    dataToTest[i][j]=Float.parseFloat(nf.format(select_ok.getFloat((String) Name[j])));
                }
            }
            if (range.size()!=0) {
                Object[][] re = new Object[columnCount][range.size()+1];
                for (int valueindex = 0; valueindex < range.size(); ++valueindex) {
                    for (int i = 0; i < dataToTest.length; ++i) {
                        for (int x = 0; x < range.get(valueindex).size(); ++x) {
                            if (dataToTest[i][valueindex] > range.get(valueindex).get(x) && dataToTest[i][valueindex] <= range.get(valueindex).get(x+1)) {
                                re[i][valueindex] = ("|"+range.get(valueindex).get(x) + "<X≤" + range.get(valueindex).get(x + 1)+"|");
                                break;
                            }
                        }
                    }
                }
                for (int i = 0; i < dataToTest.length; ++i) {
                    re[i][Name.length-1] = dataToTest[i][Name.length-1];
                    //便利旧集合没有就添加到新集合
                }
                statement.close();
                mysql.Dis_Connect();
                return re;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Object[1][1];
    }
}
