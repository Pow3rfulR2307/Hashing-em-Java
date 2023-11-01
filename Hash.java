package com.mycompany.hash;

import java.util.Random;

public class Hash {
    
    public static void procurar(Registro[]tabela, int quantValores, int tamanho, int maiorPosicao){
    
        long startSearch;
        long finishSearch;
        int comparacoes = 0;
        
        startSearch = System.nanoTime();
        
        //System.out.println("procurar com posicao "+maiorPosicao);
        
        Registro atual = tabela[maiorPosicao];
        
        while(atual.proximo!=null){
        
            atual = atual.proximo;
            comparacoes++;
        }
        
        finishSearch = System.nanoTime() - startSearch;
                
        System.out.println("Tempo para procurar o ultimo valor da maior posicao em vetor de tamanho "+tamanho+
                " e quantidade de valores "+quantValores+ ": "+(double)finishSearch/1000000+" ms");
        
        System.out.println("Comparacoes: "+comparacoes);
        
    }
    
    public static int hashFunction(int posicao, int tamanho, int option){
        
        switch(option){
        
            case 0:
                posicao = posicao%tamanho;
                break;
            case 1:             
                double A = 0.618033988749895;
                double scaledValue = posicao * A;
                double fractionalPart = scaledValue - (int)scaledValue;
                posicao = (int)(tamanho * fractionalPart);
                break;
            default:
                int hash = posicao ^ (posicao >> 16);
                if (hash < 0) {
                    hash = -hash;
                }
                posicao = hash % tamanho;
                break;
        }
        return posicao;
    }
    
    public static double inserirValor(int quantValores, int tamanho, Registro[]tabela, Random valor, 
            int colisoes, int operacao){
        
        long startInsert;
        long finishInsert;
        int maiorPosicao = 0;
        int contador = 0;
        int maiorContador = 0;
    
        startInsert = System.nanoTime();
        
        for(int i = 0; i < quantValores; i++){

            Registro r = new Registro(valor.nextInt(999999999 - 1 + 1) + 1);
            int posicao = hashFunction(r.valor, tamanho, operacao);
            
            if(tabela[posicao] == null){
            
                tabela[posicao] = r;
            }else{
                
                Registro atual = tabela[posicao];

                while (atual.proximo!=null){

                    atual = atual.proximo;
                    contador+=1;
                }
                atual.proximo = r;
                colisoes +=1;
                if(contador > maiorContador){
                
                    maiorContador = contador;
                    maiorPosicao = posicao;
                    //System.out.println("Maior posicao: "+maiorPosicao+" com valores: "+contador);
                }
                
            }
            contador = 0;
        }
        finishInsert = System.nanoTime() - startInsert;
        System.out.println("Colisoes em tamanho "+tamanho+" com "+quantValores+" valores: "+colisoes);
        procurar(tabela, quantValores, tamanho, maiorPosicao);
        
        return (double)finishInsert/1000000;
    }
    
    public static void main(String[] args) {
        
        Random gerador = new Random(69);
        int[] tamanho = {20000, 50000, 75000, 100000, 200000};
        int[] quantValores = {20000, 100000, 500000, 1000000, 5000000};
        String tipoOperacao;
        for (int operacao = 0; operacao < 3; operacao++){
            
            switch(operacao){          
                case 0:
                    tipoOperacao="Modulo por Tamanho";
                    break;
                case 1:
                    tipoOperacao="Golden Ratio (aurea)";
                    break;
                default:
                    tipoOperacao="Bitwise XOR";
            }
            System.out.println("\n-----------Operacao Hash de "+tipoOperacao+"-----------");
            for(int i = 0; i < 5; i++){ //percorre o tamanho

                int colisoes = 0;

                for(int j = 0; j < 5; j++){ //percorre quantValores
                    
                    Registro[] tabela = new Registro[tamanho[i]];

                    double tempoExecucao = inserirValor(quantValores[j], tamanho[i], tabela, gerador, 
                            colisoes, operacao);   

                    System.out.println("Tempo de inserir para tamanho "+tamanho[i]+" e quantidade de valores "+quantValores[j]
                            + ": "+tempoExecucao+" ms");
                }
            }
        }
    }
}
