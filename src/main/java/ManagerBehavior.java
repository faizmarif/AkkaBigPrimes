import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.SortedSet;
import java.util.TreeSet;

public class ManagerBehavior extends AbstractBehavior<ManagerBehavior.Command> {
  public static interface Command extends Serializable {}

  public static class InstructionCommand implements Command {
    private static final long serialVersionUID = 1L;
    private String instruction;

    public InstructionCommand(String instruction) {
      this.instruction = instruction;
    }

    public String getInstruction() {
      return instruction;
    }
  }

  private SortedSet<BigInteger> primes = new TreeSet<>();


  public static class ResultCommand implements Command {
    private static final long serialVersionUID = 1L;
    private BigInteger prime;

    public ResultCommand(BigInteger prime) {
      this.prime = prime;
    }

    public BigInteger getPrime() {
      return prime;
    }
  }

  public ManagerBehavior(ActorContext<Command> context) {
    super(context);
  }

  public static Behavior<Command> create() {
    return Behaviors.setup(ManagerBehavior::new); // I have to understand this part
  }

  @Override
  public Receive<Command> createReceive() {
    return newReceiveBuilder()
        .onMessage(InstructionCommand.class, command -> {
          if(command.getInstruction().equals("start")) {
            for (int i = 0; i < 20; i++) {
              ActorRef<WorkerBehavior.Command> worker = getContext().spawn(WorkerBehavior.create(), "workerBehaviorActor_"+i+1);
              worker.tell(new WorkerBehavior.Command("start", getContext().getSelf()));
            }
          }
          return this;
        })
        .onMessage(ResultCommand.class, command -> {
          primes.add(command.getPrime());
          System.out.println("I have received " + primes.size() + " prime numbers");
          if(primes.size() == 20) {
            primes.forEach(System.out::println);
          }
          return this;
        })
        .build();
  }
}
