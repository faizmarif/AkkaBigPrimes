import akka.actor.typed.ActorSystem;

public class Main {

  public static void main(String[] args) {
//    ActorSystem<String> actorSystem = ActorSystem.create(FirstSimpleBehavior.create(), "FirstActorSystem");
//    actorSystem.tell("say hello");
//    actorSystem.tell("who are you");
//    actorSystem.tell("create a child");
//    actorSystem.tell("This is the second message.");

    ActorSystem<ManagerBehavior.Command> actorSystem = ActorSystem.create(ManagerBehavior.create(), "managerBehaviorActor");
    actorSystem.tell(new ManagerBehavior.InstructionCommand("start"));
  }

}
