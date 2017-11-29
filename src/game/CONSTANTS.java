package game;

public class CONSTANTS
{
	public int defaultRocketXSpeed;
	public int defaultRocketYSpeed;
	public int maxGelbeRocket;
	public int maxRoteRocket;
	public int maxSchwarzeRocket;
	public int heliLifes;
	public int PowerUpDuration;
	public int ShieldDuration;
	public int leuchtkugelCount;
	public int maxHeliSpeed;
	public int minHeliSpeed;
	public int heliSpeed;
	public int blueOnlyDuration;
	public int minFuel;
	public int wkeitPowerSpeedUp;
	public String difficulty;
	public float multiply;

  public CONSTANTS(int difficulty)
  {
    if (difficulty == 0) {
      this.difficulty = "easy";
      this.multiply = 1.0F;

      easy();
    } else if (difficulty == 1) {
      this.difficulty = "medium";
      this.multiply = 1.5F;

      medium();
    } else if (difficulty == 2) {
      this.difficulty = "hard";
      this.multiply = 2.0F;

      hard();
    }
  }

  private void easy()
  {
    this.defaultRocketXSpeed = 95;
    this.defaultRocketYSpeed = 30;
    this.maxGelbeRocket = 1;
    this.maxRoteRocket = 1;
    this.maxSchwarzeRocket = 1;
    this.heliLifes = 3;
    this.PowerUpDuration = 15000;
    this.ShieldDuration = 15;
    this.leuchtkugelCount = 3;
    this.maxHeliSpeed = 100;
    this.minHeliSpeed = 50;
    this.heliSpeed = 60;
    this.blueOnlyDuration = 30;
    this.minFuel = 500;
  }

  private void medium() {
    this.defaultRocketXSpeed = 100;
    this.defaultRocketYSpeed = 30;
    this.maxGelbeRocket = 10;
    this.maxRoteRocket = 10;
    this.maxSchwarzeRocket = 10;
    this.heliLifes = 3;
    this.PowerUpDuration = 12000;
    this.ShieldDuration = 15;
    this.leuchtkugelCount = 1;
    this.maxHeliSpeed = 80;
    this.minHeliSpeed = 40;
    this.heliSpeed = 50;
    this.blueOnlyDuration = 20;
    this.minFuel = 750;
  }

  private void hard()
  {
    this.defaultRocketXSpeed = 105;
    this.defaultRocketYSpeed = 37;
    this.maxGelbeRocket = 10;
    this.maxRoteRocket = 10;
    this.maxSchwarzeRocket = 10;
    this.heliLifes = 1;
    this.PowerUpDuration = 10000;
    this.ShieldDuration = 10;
    this.leuchtkugelCount = 0;
    this.maxHeliSpeed = 80;
    this.minHeliSpeed = 40;
    this.heliSpeed = 50;
    this.blueOnlyDuration = 0;
    this.minFuel = 1000;
  }
}