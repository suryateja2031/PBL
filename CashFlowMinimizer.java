import java.util.*;

public class CashFlowMinimizer {

    static class Transaction {
        String debtor;
        String creditor;
        int amount;

        public Transaction(String debtor, String creditor, int amount) {
            this.debtor = debtor;
            this.creditor = creditor;
            this.amount = amount;
        }
    }

    static class Person {
        String name;
        int balance;

        public Person(String name, int balance) {
            this.name = name;
            this.balance = balance;
        }
    }

    // Calculate the net balance for each person from the transactions
    static void calculateNetBalance(List<Transaction> transactions, Map<String, Person> people) {
        for (Transaction t : transactions) {
            // Ensure debtor and creditor are in the people map
            people.putIfAbsent(t.debtor, new Person(t.debtor, 0));
            people.putIfAbsent(t.creditor, new Person(t.creditor, 0));

            // Update balances
            people.get(t.debtor).balance -= t.amount;
            people.get(t.creditor).balance += t.amount;
        }
    }

    // Minimize cash flow using a greedy algorithm
    static void minimizeCashFlow(Map<String, Person> people) {
        List<Person> peopleList = new ArrayList<>(people.values());
        Collections.sort(peopleList, Comparator.comparingInt(person -> person.balance));

        int i = 0, j = peopleList.size() - 1;

        while (i < j) {
            if (peopleList.get(i).balance < 0 && peopleList.get(j).balance > 0) {
                int amount = Math.min(-peopleList.get(i).balance, peopleList.get(j).balance);

                System.out.println(peopleList.get(i).name + " pays " + amount + " to " + peopleList.get(j).name);

                peopleList.get(i).balance += amount;
                peopleList.get(j).balance -= amount;

                if (peopleList.get(i).balance == 0) i++;
                if (peopleList.get(j).balance == 0) j--;
            } else {
                break;
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of people:");
        int numPeople = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Map<String, Person> people = new HashMap<>();
        System.out.println("Enter the names of the people:");
        for (int i = 0; i < numPeople; i++) {
            String name = scanner.nextLine();
            people.put(name, new Person(name, 0));
        }

        System.out.println("Enter the number of transactions:");
        int numTransactions = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        List<Transaction> transactions = new ArrayList<>();
        System.out.println("Enter each transaction (debtor creditor amount):");
        for (int i = 0; i < numTransactions; i++) {
            String debtor = scanner.next();
            String creditor = scanner.next();
            int amount = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            transactions.add(new Transaction(debtor, creditor, amount));
        }

        calculateNetBalance(transactions, people);

        System.out.println("\nMinimized cash flow transactions:");
        minimizeCashFlow(people);

        scanner.close();
    }
}
