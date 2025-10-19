package com.mindcare.service;

/**
 * Serviço simples que simula uma resposta de IA.
 * Substituam por integração real (OpenAI/Gemini) nas iterações seguintes.
 */
public class MockIAService {

    public String gerarResposta(String estadoMental, String dicasSobre, String relato, String tom) {
        StringBuilder sb = new StringBuilder();
        sb.append("Olá — obrigado por compartilhar.\n\n");
        sb.append("Estado indicado: ").append(estadoMental).append(".\n");
        if (dicasSobre != null && !dicasSobre.isBlank()) {
            sb.append("Você pediu dicas sobre: ").append(dicasSobre).append(".\n");
        }
        sb.append("\nCom base no que você escreveu:\n\"").append(shorten(relato)).append("\".\n\n");

        if ("Acolhedor".equalsIgnoreCase(tom)) {
            sb.append("Sinto muito que você esteja passando por isso. Lembre-se: suas emoções são válidas.\n");
            sb.append("Tente respirar profundamente por 2 minutos e observar pensamentos sem julgar.\n");
        } else if ("Motivacional".equalsIgnoreCase(tom)) {
            sb.append("Você está fazendo um trabalho importante ao refletir. Um passo pequeno hoje pode fazer diferença amanhã.\n");
            sb.append("Pense em uma ação pequena que você pode fazer agora para cuidar de si.\n");
        } else {
            sb.append("Aqui vão algumas sugestões práticas: escrever por 5 minutos, caminhar 10 minutos, conversar com alguém de confiança.\n");
        }

        sb.append("\nSe sentir que precisa, considere buscar apoio profissional. Se houver risco imediato, procure ajuda local.");
        return sb.toString();
    }

    private String shorten(String s) {
        if (s == null) return "";
        if (s.length() <= 200) return s;
        return s.substring(0, 200) + "...";
    }
}
