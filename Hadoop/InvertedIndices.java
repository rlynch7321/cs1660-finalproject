import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.util.GenericOptionsParser;

public class InvertedIndices
{
	public static class InvIndMapper extends Mapper<Object, Text, Text, Text>
	{
		static enum CountersEnum { INPUT_WORDS }
		
	    private Text word = new Text();
	    private String fileName = new String();
	
	    @Override
	    public void setup(Context context) throws IOException, InterruptedException
	    {
	    	fileName = ((FileSplit) context.getInputSplit()).getPath().getName();
	    }
	
	    @Override
	    public void map(Object key, Text value, Context context) throws IOException, InterruptedException
	    {
	    	String line = value.toString().toLowerCase().replaceAll("([,.!;:?\"*_\\]\\[\\)\\(])+", "").replace("--", " ");
	    	StringTokenizer itr = new StringTokenizer(line);
	    	while (itr.hasMoreTokens())
	    	{
	    		word.set(itr.nextToken());
	    		context.write(word, new Text(fileName + "~1"));
	    		Counter counter = context.getCounter(CountersEnum.class.getName(), CountersEnum.INPUT_WORDS.toString());
	    		counter.increment(1);
	    	}
	    }
	}
	
	public static class InvIndReducer extends Reducer<Text,Text,Text,Text>
	{
		private Text result = new Text();
		
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException
		{
			HashMap<String, Integer> filesAndCounts = new HashMap<String, Integer>();
			
			for (Text val : values)
			{
				String[] valArray = val.toString().split("\\|");
				
				for(int x = 0; x < valArray.length; x++)
				{
					String[] fileCountPair = valArray[x].split("~");
					
					String fileName = fileCountPair[0];
					int count = Integer.parseInt(fileCountPair[1]);
					
					if(filesAndCounts.containsKey(fileName))
						filesAndCounts.put(fileName, filesAndCounts.get(fileName) + count);
						
					else
						filesAndCounts.put(fileName, count);
				}
			}
			
			String output = "";
			
			for(Map.Entry<String, Integer> entry : filesAndCounts.entrySet())
				output += entry.getKey() + "~" + entry.getValue() + "|";
			output = output.substring(0, output.length() - 1);

			result = new Text(output);
			context.write(key, result);
		}
	}
	public static void main(String[] args) throws Exception
	{
		long startTime = System.currentTimeMillis();
		
		Configuration conf = new Configuration();
		GenericOptionsParser optionParser = new GenericOptionsParser(conf, args);
		String[] remainingArgs = optionParser.getRemainingArgs();
		
	    if (remainingArgs.length != 2)
	    {
	      System.err.println("Usage: invind <in> <out>");
	      System.exit(2);
	    }
	    
	    Job job = Job.getInstance(conf, "invind");
	    job.setJarByClass(InvertedIndices.class);
	    job.setMapperClass(InvIndMapper.class);
	    job.setCombinerClass(InvIndReducer.class);
	    job.setReducerClass(InvIndReducer.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);
	    
	    List<String> otherArgs = new ArrayList<String>();
	    
	    for (int i=0; i < remainingArgs.length; ++i)
	        otherArgs.add(remainingArgs[i]);
	
	    FileInputFormat.addInputPath(job, new Path(otherArgs.get(0)));
	    FileOutputFormat.setOutputPath(job, new Path(otherArgs.get(1)));

	    int waitForCompletionRes = job.waitForCompletion(true) ? 0 : 1;
    
	    long finishTime = System.currentTimeMillis();
	    long executionTimeInSeconds = (finishTime - startTime) / 1000;
    
	    System.out.println("v4 Execution time: " + executionTimeInSeconds + " sec");
    
	    System.exit(waitForCompletionRes);
	}
}