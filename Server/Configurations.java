package Server;

import algorithms.mazeGenerators.*;
import algorithms.search.*;

import java.io.*;
import java.util.Properties;

public class Configurations {

    public static void main(String[] args) {
        setThreadPoolNUM(4);
        setSearchingAlgorithm("DepthFirstSearch");
        setMazeAlgorithm("MyMazeGenerator");
    }

    public static void setThreadPoolNUM(int max)
    {
        try
        {
            Properties properties= new Properties();
            properties.load(new FileInputStream("resources/config.properties"));
            OutputStream temp = new FileOutputStream("resources/config.properties");
            properties.setProperty("threadPoolSize",Integer.toString(max));
            properties.store(temp,null);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void setSearchingAlgorithm(String algo)
    {
        try
        {
            Properties properties = new Properties();
            properties.load(new FileInputStream("resources/config.properties"));
            OutputStream temp = new FileOutputStream("resources/config.properties");
            properties.setProperty("searchingAlgorithm",algo);
            properties.store(temp,null);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void setMazeAlgorithm(String mazealgo)
    {
        try
        {
            Properties properties= new Properties();
            properties.load(new FileInputStream("resources/config.properties"));
            OutputStream temp = new FileOutputStream("resources/config.properties");
            properties.setProperty("generator",mazealgo);
            properties.store(temp,null);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * getter for number of threads for program
     * @return String that represent the numOfThreads that in the config file
     */
    public static int getAmountOfThreads() {
        //InputStream input = this.getClass().getClassLoader().getResourceAsStream("config.properties")
        try(InputStream input = new FileInputStream("resources/config.properties")){
            Properties p = new Properties();
            p.load(input);
            int numOfThreads;
            try {
                numOfThreads = Integer.parseInt(p.getProperty("threadPoolSize"));
            } catch (NumberFormatException e) {
                numOfThreads = 3;
            }
            return numOfThreads;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 3;
    }

    /**
     * chooses the maze generator */
    public static IMazeGenerator getGenerator() {
        try (InputStream input = new FileInputStream("resources/config.properties")) {
            Properties p = new Properties();
            p.load(input);
            String generatorName = p.getProperty("generator");;
            if(generatorName!=null && !generatorName.equals("")){
                return Configurations.getGeneratorInstance(generatorName);
            }
            else{
                return new MyMazeGenerator();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * chooses the searching algorithm */
    public static ISearchingAlgorithm getSearchingAlgorithm() {
        try (InputStream input = new FileInputStream("resources/config.properties")) {
            Properties p = new Properties();
            p.load(input);
            String searchingAlgoName= p.getProperty("searchingAlgorithm");
            if(searchingAlgoName!=null && !searchingAlgoName.equals("")){
                return Configurations.getSearchingAlgoInstance(searchingAlgoName);
            }
            else{
                return new DepthFirstSearch();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static ISearchingAlgorithm getSearchingAlgoInstance(String searchAAlgoName)
    {
        if(searchAAlgoName.equals("BreadthFirstSearch"))
            return  new BreadthFirstSearch();
        if(searchAAlgoName.equals("DepthFirstSearch"))
            return  new DepthFirstSearch();
        if(searchAAlgoName.equals("BestFirstSearch"))
             return new BestFirstSearch();
        return new DepthFirstSearch();
    }

    public static AMazeGenerator getGeneratorInstance(String generatorName)
    {
        if(generatorName.equals("EmptyMazeGenerator"))
            return  new EmptyMazeGenerator();
        if(generatorName.equals("SimpleMazeGenerator"))
            return  new SimpleMazeGenerator();
        if (generatorName.equals("MyMazeGenerator"))
            return new MyMazeGenerator();
        return new MyMazeGenerator();
    }

}
