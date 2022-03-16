class ExecTime{
    public static boolean isPrime_1(int n){
        if(n < 2) return false;
        for(int i = 2 ;i < n ; i ++) {
            if(n%i ==0 ) return false;
        }
        return true;
    }

    public static boolean isPrime_2(int n){
        if(n < 2) return false;
        for(int i = 2 ;i < n/2 ; i ++) {
            if(n%i ==0 ) return false;
        }
        return true;
    }

    public static boolean isPrime_3(int n){
        if(n < 2) return false;
        for(int i = 2 ;i < Math.sqrt(n) ; i ++) {
            if(n%i ==0 ) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        long startTime, endTime, execTime;
        int n =1000000;

        //1st method
        startTime = System.nanoTime();
        for(int i =0 ; i < n ; i++) {
            isPrime_1(1237);
            System.nanoTime();
        }
        endTime = System.nanoTime();

        execTime = endTime - startTime;
        System.out.println("prime : " + isPrime_1(1237));
        System.out.println("Execution time in nano seconds using 1st method : " + (double)(execTime/n));

        startTime = System.nanoTime();
        for(int i =0 ; i < n ; i++) {
            isPrime_2(1237);
            System.nanoTime();
        }
        endTime = System.nanoTime();

        //2nd method
        execTime = endTime - startTime;
        System.out.println("prime : " + isPrime_2(1237));
        System.out.println("Execution time in nano seconds using 2nd method : " + (double)(execTime/n));

        //3rd method
        startTime = System.nanoTime();
        for(int i =0 ; i < n ; i++) {
            isPrime_3(1237);
            System.nanoTime();
        }
        endTime = System.nanoTime();

        execTime = endTime - startTime;
        System.out.println("prime : " + isPrime_3(1237) );
        System.out.println("Execution time in nano seconds using 3rd method : " + (double)(execTime/n));

    }
}