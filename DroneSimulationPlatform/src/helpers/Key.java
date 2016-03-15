package helpers;
/*
Large Prime numbers obtained from https://primes.utm.edu/lists/small/millions/
 */

public class Key extends Object{

    public Integer getX(){
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }

    private Integer x;
    private Integer y;
    private Integer z;

    public Key(Integer[] key){
        x = key[0];
        y = key[1];
        z = key[2];
    }

    public boolean equals(Object inKey){
        Key k;
        if(inKey instanceof Key) {
            k = (Key) inKey;
        }
        else{
            return false;
        }

        if (x.equals(k.getX()) && y.equals(k.getY()) && z.equals(k.getZ())){
            return true;
        }
        return false;
    }

    public int hashCode(){
        // Multiply each value, x, y and z, by different large primes and them sum the result
        long xPrime = x * 34337;
        long yPrime = y * 40471;
        long zPrime = z * 50989;

        long hashCode = (xPrime * yPrime * zPrime)%Integer.MAX_VALUE;

        return (int) hashCode;
    }

}
