package pl.sansoft.scoreboard.interpreters;

public interface IFrameByteInterpreter{
  boolean isFrameCollecting();
  void processByte(byte b);
}