// 电梯状态接口
interface ElevatorState {
    void call(Elevator context, int floor);
    void arrive(Elevator context, int floor);
    void openDoor(Elevator context);
    void closeDoor(Elevator context);
    void fault(Elevator context);
    void repair(Elevator context);
}

// 电梯上下文
class Elevator {
    // 状态对象
    ElevatorState idleState = new IdleState();
    ElevatorState runningState = new RunningState();
    ElevatorState doorOpenState = new DoorOpenState();
    ElevatorState doorClosedState = new DoorClosedState();
    ElevatorState faultState = new FaultState();

    private ElevatorState state = idleState; // 初始状态
    private int currentFloor = 1; // 当前楼层

    // 状态切换
    public void setState(ElevatorState state) {
        this.state = state;
    }
    public ElevatorState getIdleState() { return idleState; }
    public ElevatorState getRunningState() { return runningState; }
    public ElevatorState getDoorOpenState() { return doorOpenState; }
    public ElevatorState getDoorClosedState() { return doorClosedState; }
    public ElevatorState getFaultState() { return faultState; }

    public int getCurrentFloor() { return currentFloor; }
    public void setCurrentFloor(int floor) { this.currentFloor = floor; }

    // 操作接口，委托当前状态
    public void call(int floor) { state.call(this, floor); }
    public void arrive(int floor) { state.arrive(this, floor); }
    public void openDoor() { state.openDoor(this); }
    public void closeDoor() { state.closeDoor(this); }
    public void fault() { state.fault(this); }
    public void repair() { state.repair(this); }
}

// 各具体状态实现
class IdleState implements ElevatorState {
    public void call(Elevator context, int floor) {
        System.out.println("[Idle] 收到召唤，准备前往 " + floor + " 层");
        context.setState(context.getRunningState());
        context.arrive(floor); // 模拟直接到达
    }
    public void arrive(Elevator context, int floor) {
        System.out.println("[Idle] 电梯未运行，不能到达新楼层");
    }
    public void openDoor(Elevator context) {
        System.out.println("[Idle] 开门");
        context.setState(context.getDoorOpenState());
    }
    public void closeDoor(Elevator context) {
        System.out.println("[Idle] 门已关");
    }
    public void fault(Elevator context) {
        System.out.println("[Idle] 发生故障，切换到故障状态");
        context.setState(context.getFaultState());
    }
    public void repair(Elevator context) {
        System.out.println("[Idle] 电梯正常，无需修理");
    }
}

class RunningState implements ElevatorState {
    public void call(Elevator context, int floor) {
        System.out.println("[Running] 已在运行中，请稍后");
    }
    public void arrive(Elevator context, int floor) {
        System.out.println("[Running] 已到达 " + floor + " 层");
        context.setCurrentFloor(floor);
        context.setState(context.getDoorOpenState());
        context.openDoor();
    }
    public void openDoor(Elevator context) {
        System.out.println("[Running] 运行中不能开门");
    }
    public void closeDoor(Elevator context) {
        System.out.println("[Running] 门已关");
    }
    public void fault(Elevator context) {
        System.out.println("[Running] 发生故障，紧急停运！");
        context.setState(context.getFaultState());
    }
    public void repair(Elevator context) {
        System.out.println("[Running] 电梯运行中，不能修理");
    }
}

class DoorOpenState implements ElevatorState {
    public void call(Elevator context, int floor) {
        System.out.println("[DoorOpen] 门开着，请关门后再操作");
    }
    public void arrive(Elevator context, int floor) {
        System.out.println("[DoorOpen] 门已开，不移动");
    }
    public void openDoor(Elevator context) {
        System.out.println("[DoorOpen] 门已经开了");
    }
    public void closeDoor(Elevator context) {
        System.out.println("[DoorOpen] 关门");
        context.setState(context.getDoorClosedState());
    }
    public void fault(Elevator context) {
        System.out.println("[DoorOpen] 发生故障，切换到故障状态");
        context.setState(context.getFaultState());
    }
    public void repair(Elevator context) {
        System.out.println("[DoorOpen] 无需修理");
    }
}

class DoorClosedState implements ElevatorState {
    public void call(Elevator context, int floor) {
        System.out.println("[DoorClosed] 收到楼层指令，电梯准备运行");
        context.setState(context.getRunningState());
        context.arrive(floor); // 模拟直接到达
    }
    public void arrive(Elevator context, int floor) {
        System.out.println("[DoorClosed] 未运行，不能到达新楼层");
    }
    public void openDoor(Elevator context) {
        System.out.println("[DoorClosed] 开门");
        context.setState(context.getDoorOpenState());
    }
    public void closeDoor(Elevator context) {
        System.out.println("[DoorClosed] 门已关");
    }
    public void fault(Elevator context) {
        System.out.println("[DoorClosed] 发生故障，切换到故障状态");
        context.setState(context.getFaultState());
    }
    public void repair(Elevator context) {
        System.out.println("[DoorClosed] 无需修理");
    }
}

class FaultState implements ElevatorState {
    public void call(Elevator context, int floor) {
        System.out.println("[Fault] 电梯故障，无法响应");
    }
    public void arrive(Elevator context, int floor) {
        System.out.println("[Fault] 电梯故障，无法到达新楼层");
    }
    public void openDoor(Elevator context) {
        System.out.println("[Fault] 电梯故障，门无法打开");
    }
    public void closeDoor(Elevator context) {
        System.out.println("[Fault] 电梯故障，门无法关闭");
    }
    public void fault(Elevator context) {
        System.out.println("[Fault] 已处于故障状态");
    }
    public void repair(Elevator context) {
        System.out.println("[Fault] 电梯维修完成，回到空闲状态");
        context.setState(context.getIdleState());
    }
}

// 演示
public class ElevatorDemo {
    public static void main(String[] args) {
        Elevator elevator = new Elevator();

        elevator.call(5);          // 从Idle到Running，再到DoorOpen
        elevator.closeDoor();      // DoorOpen->DoorClosed
        elevator.call(2);          // DoorClosed->Running->DoorOpen
        elevator.fault();          // DoorOpen->Fault
        elevator.repair();         // Fault->Idle
        elevator.openDoor();       // Idle->DoorOpen
        elevator.closeDoor();      // DoorOpen->DoorClosed
    }
}
