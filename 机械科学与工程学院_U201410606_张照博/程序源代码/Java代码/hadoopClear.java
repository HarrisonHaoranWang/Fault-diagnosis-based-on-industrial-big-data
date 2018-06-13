//import java.io.IOException;
//
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.io.IntWritable;
//import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapreduce.Job;
//import org.apache.hadoop.mapreduce.Mapper;
//import org.apache.hadoop.mapreduce.Reducer;
//import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
//import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
//import org.apache.hadoop.util.GenericOptionsParser;
//
//public class hadoopClear {
//    //map�������е�value���Ƶ�������ݵ�key�ϣ���ֱ�����
//    public static class Map extends Mapper<Object,Text,Text,Text>{
//        //ÿ������
//        private static Text line=new Text();
//        //ʵ��map����
//        public void map(Object key,Text value,Context context) throws IOException,InterruptedException{
//            String line = value.toString();
//            String[] values = line.split(" ");
//            line = "";
//            for(int i=0; i<values.length -1 ;++i)
//            {
//                line += values[i];
//            }
//            context.write(new Text(line), new Text(""));
//        }
//    }
//    //reduce�������е�key���Ƶ�������ݵ�key�ϣ���ֱ�����
//    public static class Reduce extends Reducer<Text,Text,Text,Text>{
//        //ʵ��reduce����
//        public void reduce(Text key,Iterable<Text> values,Context context)
//                throws IOException,InterruptedException{
//            context.write(key, new Text(""));
//        }
//    }
//
//    public static void main(String[] args) throws Exception{
//        Configuration conf = new Configuration();
//        //��仰�ܹؼ�
//        conf.set("mapred.job.tracker", "node61:9001");
//        String[] ioArgs=new String[]{"dedup_in","dedup_out"};
//        String[] otherArgs = new GenericOptionsParser(conf, ioArgs).getRemainingArgs();
//        if (otherArgs.length != 2) {
//            System.err.println("Usage: Data Deduplication <in> <out>");
//            System.exit(2);
//        }
//        Job job = new Job(conf, "Data Deduplication");
//        job.setJarByClass(hadoopClear.class);
//        //����Map��Combine��Reduce������
//        job.setMapperClass(Map.class);
//        job.setCombinerClass(Reduce.class);
//        job.setReducerClass(Reduce.class);
//        //�����������
//        job.setOutputKeyClass(Text.class);
//        job.setOutputValueClass(Text.class);
//        //������������Ŀ¼
//        FileInputFormat.addInputPath(job, new Path(args[0]));
//        FileOutputFormat.setOutputPath(job, new Path(args[1]));
//        System.exit(job.waitForCompletion(true) ? 0 : 1);
//    }
//}
//
//

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
/**
 * Created by  ZZB on 2018/6/10.
 */
public class hadoopClear {
    public static void main(String[] args)throws Exception{
        //�������ö���
        Configuration conf = new Configuration();
        //����job����
        Job job = Job.getInstance(conf,"hadoopClear");
        //��������job����
        job.setJarByClass(hadoopClear.class);
        //����mapper ��
        job.setMapperClass(ZZB_Mapper.class);
        //����reduce ��
        job.setReducerClass(ZZB_Reducer.class);
        //����map�����key value
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        //����reduce ����� key value
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        //�������������·��
        FileInputFormat.setInputPaths(job, new Path(args[1]));
        FileOutputFormat.setOutputPath(job, new Path(args[2]));
        //�ύjob
        boolean b = job.waitForCompletion(true);
        if(!b){
            System.out.println("wordcount task fail!");
        }
    }
}