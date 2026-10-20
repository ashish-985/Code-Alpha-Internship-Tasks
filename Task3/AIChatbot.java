import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

/**
 * CodeAlpha Java Programming Internship - Task 3
 * Rule-based AI Chatbot (console application).
 *
 * Demonstrates simple NLP concepts: text normalisation, tokenisation,
 * keyword/synonym matching, intent detection, and a fallback response.
 */
public class AIChatbot {

    private static final Map<String, String[]> INTENTS = new LinkedHashMap<>();
    private static final Map<String, String> RESPONSES = new LinkedHashMap<>();

    static {
        addIntent("greeting",
                new String[]{"hello", "hi", "hey", "good morning", "good afternoon", "good evening"},
                "Hello! I'm AlphaBot. How can I help you today?");

        addIntent("name",
                new String[]{"your name", "who are you", "what are you"},
                "I'm AlphaBot, a simple rule-based Java chatbot created for the CodeAlpha internship.");

        addIntent("help",
                new String[]{"help", "what can you do", "options", "commands"},
                "I can answer basic questions about greetings, my name, Java, the internship, and how to contact support. Type 'bye' to exit.");

        addIntent("java",
                new String[]{"java", "oop", "object oriented", "programming language"},
                "Java is a general-purpose, object-oriented programming language. Core OOP ideas include encapsulation, inheritance, polymorphism, and abstraction.");

        addIntent("internship",
                new String[]{"codealpha", "internship", "task", "project"},
                "For your CodeAlpha internship, complete the required project tasks, push source code to GitHub, share a demo video on LinkedIn, and submit through the provided submission form.");

        addIntent("contact",
                new String[]{"contact", "support", "email", "phone"},
                "The task brief lists services@codealpha.tech and services.codealpha@gmail.com for email support.");

        addIntent("thanks",
                new String[]{"thanks", "thank you", "thx"},
                "You're welcome! Is there anything else you'd like to ask?");

        addIntent("goodbye",
                new String[]{"bye", "goodbye", "see you", "exit", "quit"},
                "Goodbye! Thanks for chatting with AlphaBot.");
    }

    private static void addIntent(String intent, String[] phrases, String response) {
        INTENTS.put(intent, phrases);
        RESPONSES.put(intent, response);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("====================================");
        System.out.println("       Welcome to AlphaBot");
        System.out.println(" Type 'help' for examples, 'bye' to exit.");
        System.out.println("====================================");

        while (true) {
            System.out.print("\nYou: ");
            String input = scanner.nextLine();
            String normalized = normalize(input);

            if (normalized.isEmpty()) {
                System.out.println("AlphaBot: Please type a message so I can try to help.");
                continue;
            }

            String intent = detectIntent(normalized);
            String response = intent == null
                    ? "I'm not sure I understood that. Try asking about Java, the internship, or type 'help' for examples."
                    : RESPONSES.get(intent);

            System.out.println("AlphaBot: " + response);

            if ("goodbye".equals(intent)) {
                break;
            }
        }

        scanner.close();
    }

    private static String normalize(String input) {
        return input.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private static String detectIntent(String normalizedInput) {
        String[] tokens = normalizedInput.split(" ");
        // Tokenisation is kept explicit to demonstrate a basic NLP preprocessing step.
        for (Map.Entry<String, String[]> entry : INTENTS.entrySet()) {
            for (String phrase : entry.getValue()) {
                String normalizedPhrase = normalize(phrase);
                if (normalizedInput.equals(normalizedPhrase)
                        || normalizedInput.contains(" " + normalizedPhrase + " ")
                        || Arrays.asList(tokens).contains(normalizedPhrase)) {
                    return entry.getKey();
                }
                // Multi-word phrases are matched against the normalized user message.
                if (normalizedPhrase.contains(" ") && normalizedInput.contains(normalizedPhrase)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }
}
