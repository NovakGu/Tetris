package tetris;

public class TetrisMain {

    public static void main(String[] args) {
        System.out.println("Hello, Tetris!");
        try {
            ProgramArgs a = ProgramArgs.parseArgs(args);
            Tetris tetris = new Tetris(a.getFPS(), a.getSpeed(), a.getSequence());

        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
    }
}


