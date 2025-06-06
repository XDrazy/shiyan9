// 模拟图片对象
class Image {
    private int size; // KB
    public Image(int size) { this.size = size; }
    public int getSize() { return size; }
}

// 抽象中介者
abstract class ChatMediator {
    public abstract void register(Member m);
    public abstract void sendText(String from, String to, String msg);
    public abstract void sendImage(String from, String to, Image img);
}

// 具体中介者
class ChatRoom extends ChatMediator {
    private java.util.Map<String, Member> members = new java.util.HashMap<>();
    private static final int MAX_IMAGE_SIZE = 1000; // KB

    public void register(Member m) {
        members.put(m.getName(), m);
    }

    // 过滤不雅字符
    private String filter(String msg) {
        String[] bad = {"sb", "傻逼", "fuck"};
        String filtered = msg;
        for (String w : bad)
            filtered = filtered.replace(w, "**");
        return filtered;
    }

    public void sendText(String from, String to, String msg) {
        if (members.containsKey(to))
            members.get(to).receiveText(from, filter(msg));
    }

    public void sendImage(String from, String to, Image img) {
        if (img.getSize() > MAX_IMAGE_SIZE) {
            members.get(from).receiveText("系统", "发送图片失败，图片太大！");
            return;
        }
        if (members.containsKey(to))
            members.get(to).receiveImage(from, img);
    }
}

// 抽象会员
abstract class Member {
    protected String name;
    protected ChatMediator mediator;

    public Member(String name, ChatMediator mediator) {
        this.name = name; this.mediator = mediator;
        mediator.register(this);
    }
    public String getName() { return name; }
    public void sendText(String to, String msg) {
        mediator.sendText(name, to, msg);
    }
    public void receiveText(String from, String msg) {
        System.out.println("[" + name + "] 收到[" + from + "]文本: " + msg);
    }
    public void sendImage(String to, Image img) {
        System.out.println("[" + name + "] 你没有权限发送图片！");
    }
    public void receiveImage(String from, Image img) {
        System.out.println("[" + name + "] 你无法接收图片！");
    }
}

// 普通会员
class CommonMember extends Member {
    public CommonMember(String name, ChatMediator mediator) { super(name, mediator); }
}

// 钻石会员
class DiamondMember extends Member {
    public DiamondMember(String name, ChatMediator mediator) { super(name, mediator); }
    @Override
    public void sendImage(String to, Image img) {
        mediator.sendImage(name, to, img);
    }
    @Override
    public void receiveImage(String from, Image img) {
        System.out.println("[" + name + "] 收到[" + from + "]的图片，大小：" + img.getSize() + "KB");
    }
}

// 演示入口
public class MediatorChatDemo {
    public static void main(String[] args) {
        ChatRoom chatRoom = new ChatRoom();

        Member alice = new CommonMember("Alice", chatRoom);
        Member bob = new DiamondMember("Bob", chatRoom);
        Member tom = new DiamondMember("Tom", chatRoom);

        alice.sendText("Bob", "你好Bob，傻逼！");
        bob.sendText("Alice", "你好Alice！");
        bob.sendImage("Alice", new Image(900));
        bob.sendImage("Alice", new Image(1200)); // 超出限制
        alice.sendImage("Bob", new Image(100));  // 没权限
        tom.sendText("Alice", "fuck you!");
    }
}
