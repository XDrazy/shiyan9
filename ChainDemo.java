// 采购请求
class PurchaseRequest {
    private String purpose;
    private double amount;
    public PurchaseRequest(String purpose, double amount) {
        this.purpose = purpose;
        this.amount = amount;
    }
    public double getAmount() { return amount; }
    public String getPurpose() { return purpose; }
}

// 职责链处理者抽象类
abstract class Approver {
    protected String name;
    protected Approver next; // 下一个处理者
    public Approver(String name) { this.name = name; }
    public void setNext(Approver next) { this.next = next; }
    public abstract void approve(PurchaseRequest req);
}

// 主任
class Director extends Approver {
    public Director(String name) { super(name); }
    public void approve(PurchaseRequest req) {
        if (req.getAmount() <= 10000) {
            System.out.println("主任[" + name + "]审批通过：" + req.getPurpose() + "，金额：" + req.getAmount());
        } else if (next != null) {
            next.approve(req);
        }
    }
}

// 部门经理
class Manager extends Approver {
    public Manager(String name) { super(name); }
    public void approve(PurchaseRequest req) {
        if (req.getAmount() <= 50000) {
            System.out.println("部门经理[" + name + "]审批通过：" + req.getPurpose() + "，金额：" + req.getAmount());
        } else if (next != null) {
            next.approve(req);
        }
    }
}

// 副总经理
class VicePresident extends Approver {
    public VicePresident(String name) { super(name); }
    public void approve(PurchaseRequest req) {
        if (req.getAmount() <= 100000) {
            System.out.println("副总经理[" + name + "]审批通过：" + req.getPurpose() + "，金额：" + req.getAmount());
        } else if (next != null) {
            next.approve(req);
        }
    }
}

// 总经理
class President extends Approver {
    public President(String name) { super(name); }
    public void approve(PurchaseRequest req) {
        if (req.getAmount() <= 200000) {
            System.out.println("总经理[" + name + "]审批通过：" + req.getPurpose() + "，金额：" + req.getAmount());
        } else if (next != null) {
            next.approve(req);
        }
    }
}

// 董事会或会议
class Congress extends Approver {
    public Congress(String name) { super(name); }
    public void approve(PurchaseRequest req) {
        System.out.println("董事会[" + name + "]审批（会议讨论）：" + req.getPurpose() + "，金额：" + req.getAmount());
    }
}

// 演示主程序
public class ChainDemo {
    public static void main(String[] args) {
        Approver director = new Director("张主任");
        Approver manager = new Manager("李经理");
        Approver vp = new VicePresident("王副总");
        Approver president = new President("赵总");
        Approver congress = new Congress("公司高层");

        // 设置链条
        director.setNext(manager);
        manager.setNext(vp);
        vp.setNext(president);
        president.setNext(congress);

        // 测试不同金额的采购
        PurchaseRequest r1 = new PurchaseRequest("购买办公用品", 8000);
        PurchaseRequest r2 = new PurchaseRequest("设备维护", 30000);
        PurchaseRequest r3 = new PurchaseRequest("新电脑采购", 80000);
        PurchaseRequest r4 = new PurchaseRequest("实验室升级", 180000);
        PurchaseRequest r5 = new PurchaseRequest("公司扩建", 300000);

        director.approve(r1);
        director.approve(r2);
        director.approve(r3);
        director.approve(r4);
        director.approve(r5);
    }
}
