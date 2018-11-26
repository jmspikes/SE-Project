package programming.spikes.jon.help_a_hog;

public class SportPic {

    String name;
    String record;
    int thumbnail;

    public SportPic(){


    }

    public SportPic(String name, String record, int thumbnail){
        this.name = name;
        this.record = record;
        this.thumbnail = thumbnail;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getRecord(){
        return record;
    }

    public void setRecord(String record){
        this.record = record;
    }

    public int getThumbnail(){
        return thumbnail;
    }

    public void setThumbnail(int thumbnail){
        this.thumbnail = thumbnail;
    }

}
