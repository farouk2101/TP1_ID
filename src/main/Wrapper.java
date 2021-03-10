package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Wrapper {

    ArrayList<Column> columns;
    File file;

    public Wrapper(){
        columns = new ArrayList<>();
    }

    public void parse(String path) throws IOException {
        file = new File(path);
        Scanner scanner = new Scanner(file);
        String currentLine =  scanner.nextLine();

        //Une façon de faire

        /*String[] tabline = parseLine(currentLine);
        for(int i = 0; i < tabline.length; i++){
            columns.add(new Column((tabline[i])));
            if(i == 1){
                columns.get(i).setPrimaryKey(true);
            }
            else{
                columns.get(i).setPrimaryKey(false);
            }
        }*/

        //Autre façon de faire

        for (String name: parseLine(currentLine)) {
            columns.add(new Column(name));
        }
        for(int i =0; i < columns.size(); i++){
            if(i == 0){
                columns.get(i).setPrimaryKey(true);
                columns.get(i).setNotNull(true);
            }
            else{
                columns.get(i).setPrimaryKey(false);
                columns.get(i).setNotNull(false);
            }
        }

        while(scanner.hasNextLine()){
            currentLine =  scanner.nextLine();
            String[] items = parseLine(currentLine);
            for (int i=0; i<columns.size(); i++) {
                Column column = columns.get(i);
                String item = items[i];
                int size = item.length();

                // On ajoute l'item à la colonne
                column.addItem(item);

                // Definir maxSize
                if (size > column.getMaxSize()) {
                    column.setMaxSize(size);
                }

                // Definir le type
                Type type = defineType(item);
                if (column.getType() == Type.UNKNOWN) column.setType(type);
                else if ((column.getType() == Type.DECIMAL && type == Type.INTEGER) || (type == Type.DECIMAL && column.getType() == Type.DECIMAL)) column.setType(Type.DECIMAL );
                else if (column.getType() != type) column.setType(Type.VARCHAR);
            }
        }

    }

    private Type defineType(String item) {
        if (item.matches("^[0-9]+$")) return Type.INTEGER;
        if (item.matches("^[0-9]+\\.[0-9]+$")) return Type.DECIMAL;
        if (item.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$")) return Type.DATE;
        return Type.VARCHAR;
    }

    private String[] parseLine(String line){
        return line.split(";");
    }

    public ArrayList<Column> getColumns() {
        return columns;
    }

    public File getFile() {
        return file;
    }

    public void setColumns(ArrayList<Column> columns) {
        this.columns = columns;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
