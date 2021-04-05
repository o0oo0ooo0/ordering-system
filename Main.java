import java.util.Scanner;

public class Main {
    static final Scanner in = new Scanner(System.in);

    static final int MAX = 4;
    static final String[] names = new String[MAX];
    static final String[] dishMegs = new String[MAX];
    static final int[] times = new int[MAX];
    static final String[] addresses = new String[MAX];
    static final boolean[] states = new boolean[MAX];
    static final double[] sumPrices = new double[MAX];

    static final String[] dishNames = {"红烧带鱼", "鱼香肉丝", "时令蔬菜"};
    static final double[] prices = {38.0, 20.0, 10.0};
    static final int[] praiseNums = new int[3];

    static final boolean reserved = false;
    static final boolean signed = true;
    static final int NoOrder = 1;

    public static void main(String[] args) {
        welcome();
        while (true) {
            menu();
        }
    }

    public static void welcome() {
        System.out.println("欢迎使用订餐系统");
    }

    public static void menu() {
        System.out.println("**************************");
        System.out.println("1. 我要订餐");
        System.out.println("2. 查看餐袋");
        System.out.println("3. 签收订单");
        System.out.println("4. 删除订单");
        System.out.println("5. 我要点赞");
        System.out.println("6. 退出系统");
        System.out.println("**************************");

        int choice = in.nextInt();
        switch (choice) {
            case 1:
                order();
                break;
            case 2:
                view();
                break;
            case 3:
                sign();
                break;
            case 4:
                del();
                break;
            case 5:
                star();
                break;
            case 6:
                exit();
                break;
            default:
                break;
        }

        System.out.println("输入0返回");
        choice = in.nextInt();
        if (choice != 0) {
            exit();
        }
    }

    public static void order() {
        System.out.println("***我要订餐***");
        boolean isIdle = false;
        for (int i = 0; i < names.length && !isIdle; ++i) {
            if (names[i] == null) {
                isIdle = true;
                System.out.print("请输入订餐人姓名: ");
                getName(i);

                printRealMenu();

                System.out.print("请选择您要点的菜品序号: ");
                int choice = getChoice();

                System.out.print("请选择您需要的份数: ");
                int portion = getPortion();
                dishMegs[i] = String.format("%s%d份", dishNames[choice], portion);

                System.out.print("请输入送餐时间(送餐时间是10点至20点间整点送餐): ");
                readTime(i);
                System.out.print("请输入送餐地址: ");
                readAddress(i);

                printReceipt(i, choice, portion);

                states[i] = reserved;
            }
        }
        if (!isIdle) {
            System.out.println("订单已满!");
        }
    }

    private static void printReceipt(int i, int choice, int portion) {
        System.out.println("订餐成功!");
        System.out.printf("您订的是: %s%d份\n", dishNames[choice], portion);
        System.out.printf("送餐时间: %d点\n", times[i]);

        double expense = prices[choice] * portion;
        double shipping = expense < 50 ? 5.0 : 0.0;
        sumPrices[i] = expense + shipping;
        System.out.printf("餐费: %.1f元, 送餐费%.1f元, 总计: %.1f元\n", expense, shipping, sumPrices[i]);
    }

    private static void printRealMenu() {
        System.out.println("序号\t\t菜名\t\t单价\t\t点赞数");
        for (int j = 0; j < dishNames.length; ++j) {
            System.out.printf("%d\t\t%s\t%.1f\t%d\n", j + 1, dishNames[j], prices[j], praiseNums[j]);
        }
    }

    private static int getPortion() {
        int portion = in.nextInt();
        while (portion < 0) {
            System.out.print("份数错误, 请重新输入: ");
            portion = in.nextInt();
        }
        return portion;
    }

    private static void getName(int i) {
        names[i] = in.next();
        while (names[i] == null) {
            System.out.print("姓名错误, 请重新输入: ");
            names[i] = in.next();
        }
    }

    private static void readAddress(int i) {
        addresses[i] = in.next();
        while (addresses[i] == null) {
            System.out.print("地址错误, 请重新输入: ");
            addresses[i] = in.next();
        }
    }

    private static void readTime(int i) {
        times[i] = in.nextInt();
        while (times[i] < 10 || times[i] > 20) {
            System.out.print("送餐时间错误, 请重新输入: ");
            times[i] = in.nextInt();
        }
    }

    private static int getChoice() {
        int choice = in.nextInt();
        while (choice < 1 || choice > 4) {
            System.out.print("序号错误, 请重新输入: ");
            choice = in.nextInt();
        }
        return choice - 1;
    }

    public static void view() {
        System.out.println("***查看餐袋***");
        printOrder();
    }

    private static int printOrder() {
        if (names[0] == null) {
            System.out.println("没有订单信息!");
            return NoOrder;
        } else {
            System.out.println("序号\t订餐人\t餐品信息\t送餐时间\t送餐地址\t总金额\t订单状态");
            for (int i = 0; i < names.length && names[i] != null; ++i) {
                System.out.printf("%d\t%s\t\t%s\t%d点\t%s\t%.1f\t%s\n", i + 1, names[i], dishMegs[i], times[i], addresses[i], sumPrices[i], states[i] ? "已完成" : "已预定");
            }
        }
        return 0;
    }

    public static void sign() {
        System.out.println("***签收订单***");
        if (printOrder() != NoOrder) {
            System.out.print("请选择要签收的订单序号: ");
            int choice = getChoice();
            if (names[choice] == null) {
                System.out.println("订单不存在!");
            } else if (states[choice] == signed) {
                System.out.println("订单已被签收过, 不能重复签收!");
            } else {
                states[choice] = signed;
                System.out.println("签收成功!");
            }
        }
    }

    public static void del() {
        System.out.println("***删除订单***");
        if (printOrder() != NoOrder) {
            System.out.print("请选择要删除的订单序号: ");
            int choice = getChoice();
            if (names[choice] == null) {
                System.out.println("订单不存在!");
            } else if (states[choice] == reserved) {
                System.out.println("订单处于预定状态, 不能删除!");
            } else {
                deleteOrder(choice);
                System.out.println("删除成功!");
            }
        }

    }

    private static void deleteOrder(int choice) {
        int i;
        for (i = choice + 1; i < names.length && names[i] != null; ++i) {
            moveForwardOrder(i);
        }
        initializeOrder(i - 1);
    }

    private static void moveForwardOrder(int i) {
        names[i - 1] = names[i];
        dishMegs[i - 1] = dishMegs[i];
        times[i - 1] = times[i];
        addresses[i - 1] = addresses[i];
        states[i - 1] = states[i];
        sumPrices[i - 1] = sumPrices[i];
    }

    private static void initializeOrder(int i) {
        names[i] = null;
        dishMegs[i] = null;
        times[i] = 0;
        addresses[i] = null;
        states[i] = reserved;
        sumPrices[i] = 0;
    }

    public static void star() {
        System.out.println("***我要点赞***");
        printRealMenu();
        System.out.print("请选择您要点赞的菜品序号: ");
        int choice = getChoice();
        ++praiseNums[choice];
        System.out.println("点赞成功!");
    }

    public static void exit() {
        System.out.println("谢谢使用, 欢迎下次光临!");
        System.exit(0);
    }
}
