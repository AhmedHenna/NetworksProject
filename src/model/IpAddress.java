package model;

public class IpAddress {
    private int firstChunk;
    private int secondChunk;
    private int thirdChunk;
    private int fourthChunk;

    public IpAddress(int firstChunk, int secondChunk, int thirdChunk, int fourthChunk) {
        this.firstChunk = firstChunk;
        this.secondChunk = secondChunk;
        this.thirdChunk = thirdChunk;
        this.fourthChunk = fourthChunk;
    }

    public int getFirstChunk() {
        return firstChunk;
    }

    public int getSecondChunk() {
        return secondChunk;
    }

    public int getThirdChunk() {
        return thirdChunk;
    }

    public int getFourthChunk() {
        return fourthChunk;
    }

    @Override
    public String toString() {
        return firstChunk + "." + secondChunk + "." + thirdChunk + "." + fourthChunk;
    }
}
