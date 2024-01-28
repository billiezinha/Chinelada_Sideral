package com.zetcode;

public interface Commons {

    int LARGURA_TABULEIRO = 358;
    int ALTURA_TABULEIRO = 350;
    int BORDA_DIREITA = 30;
    int BORDA_ESQUERDA = 5;

    int SOLO = 290;
    int ALTURA_BOMBA = 5;

    int ALTURA_FILHO = 12;  // Alterado de ALTURA_ALIEN para ALTURA_FILHO
    int LARGURA_FILHO = 12;  // Alterado de LARGURA_ALIEN para LARGURA_FILHO
    int FILHO_X_INICIAL = 150;  // Alterado de ALIEN_X_INICIAL para FILHO_X_INICIAL
    int FILHO_Y_INICIAL = 5;  // Alterado de ALIEN_Y_INICIAL para FILHO_Y_INICIAL

    int MOVIMENTO_ABAIXO = 15;
    int NUMERO_DE_FILHOS_PARA_DESTRUIR = 24;  // Alterado de NUMERO_DE_ALIENS_PARA_DESTRUIR para NUMERO_DE_FILHOS_PARA_DESTRUIR
    int CHANCE = 5;
    int DELAY = 17;
    int LARGURA_MAE = 15;
    int ALTURA_MAE = 10;
    int FILHO_INICIAL_X = 0;
    int FILHO_INICIAL_Y = 0;
    int BOARD_WIDTH = 0;
}
