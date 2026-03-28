public class Ref{
    public static void main(String[] args) {
        Client c = new Client();
        Villa v = new Villa();
        Apartment a = new Apartment();
        c.display(a);
        c.display(v);
    }
}

interface Home{
    void display();
}

class Villa implements Home{
    public void display(){
        System.out.println("villa for client");
    }
}

class Apartment implements Home{
    public void display(){
        System.out.println("apartment for client");
    }
}

class Client{
    public void display(Home h){
        h.display();
    }
}