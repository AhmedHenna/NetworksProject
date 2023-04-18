package model;

public class IpAddress {
    private final int firstChunk;
    private final int secondChunk;
    private final int thirdChunk;
    private final int fourthChunk;

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
