public class Number {
    private String a;
    private String b;
    private String c;
    private String d;

    public Number(String a, String b, String c, String d) {
        this.a = a;
        if(b!=a){
            this.b = b;
        }
        if(c!=a && c!=b){
            this.c = c;
        }
        if(d!=a && d!=b && d!=c){
            this.d = d;
        }

    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        if(b!=a){
            this.b = b;
        }
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        if(c!=a && c!=b){
            this.c = c;
        }
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        if(d!=a && d!=b && d!=c){
            this.d = d;
        }
    }

}
