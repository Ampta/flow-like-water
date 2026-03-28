enum Status {
    SUCCESS(200),
    FAILURE(500),
    CONTRA,
    PENDING(300);

    private int code;

    private Status(){
        this.code = 000;
    }

    private Status(int code) {
        this.code = code;
    }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
}

public class EnumDemo {
    public static void main(String[] args) {
        Status s = Status.CONTRA;
        System.out.println("The status is: " + s + " with code: " + s.getCode());

        System.out.println(s.ordinal());

        Status[] statuses = Status.values();
        for (Status status : statuses) {
            System.out.println(status);
        }

        if(s == Status.SUCCESS) {
            System.out.println("Operation was successful!");
        } else if (s == Status.FAILURE) {
            System.out.println("Operation failed.");
        } else {
            System.out.println("Operation is pending.");
        }

        Status s2 = Status.FAILURE;

        switch (s2) {
            case SUCCESS:
                System.out.println("Operation was successful!");        
                break;
            case FAILURE:
                System.out.println("Operation failed.");
                break;
            case PENDING:
                System.out.println("Operation is pending.");
                break;
            default:
                break;
        }
    }
}
