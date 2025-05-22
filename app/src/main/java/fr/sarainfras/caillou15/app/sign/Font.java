package fr.sarainfras.caillou15.app.sign;

import org.apache.commons.lang3.NotImplementedException;

import javax.swing.*;

public class Font {
    public enum SignFont {
        // appelé serre poour les polices que j'ai modifiées pour que les espaces soient corrects
        L1serre, L2serre, L4serre;

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public static double getTextLength(String text, SignFont font, int numeroGamme, boolean L2grand) {
        double hc = font == SignFont.L2serre && L2grand ? DirectionalSign.gammes[numeroGamme+1] : DirectionalSign.gammes[numeroGamme];
        double hc_petit = font == SignFont.L2serre && L2grand ? DirectionalSign.gammes[numeroGamme-1] : DirectionalSign.gammes[numeroGamme-2];

        if (text.isEmpty()) {return 0;}
        else {
            double totalLength = 0;
            char[] charArray = text.toCharArray();
            boolean indice = false;
            boolean exposant = false;
            boolean juste_balscule = false;
            for (int i = 0; i < charArray.length; i++) {
                if (charArray[i] == '_') { indice = !indice;  juste_balscule = true; }
                if (charArray[i] == '^') { exposant = !exposant;  juste_balscule = true; }

                if ( (indice || exposant) && !juste_balscule)
                    totalLength += getLetterLength(charArray[i], font, hc_petit);
                else
                    totalLength += getLetterLength(charArray[i], font, hc);

                if (i != charArray.length - 1) {
                    if ( (indice || exposant))
                        totalLength
                                += getGapLengthBetweenLetter(charArray[i], charArray[i + 1], font, numeroGamme-2);
                    else
                        totalLength
                            += getGapLengthBetweenLetter(charArray[i], charArray[i + 1], font, numeroGamme);
                }

                juste_balscule = false;
            }
            return totalLength;
        }

    }

    public static double getLetterLength(char c, SignFont font, double hc) {
        double base = -1;

        if (Character.isLowerCase(c) && font != SignFont.L4serre) return getLetterLength(Character.toUpperCase(c), font, hc);

        switch (font) {
            case L1serre -> {
                base= switch (c) {
                    case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä' -> 76.5;
                    case 'B' -> 65;
                    case 'C' -> 68.75;
                    case 'D', 'H', 'K', 'N', 'U', 'Z' -> 68;
                    case 'E', 'É', 'È', 'Ê', 'Ë', 'T' -> 62.5;
                    case 'F' -> 60.25;
                    case 'G' -> 71.75;
                    case 'I', '.' -> 19.5;
                    case 'J' -> 62;
                    case 'L' -> 56.5;
                    case 'M' -> 93.25;
                    case 'O', 'Q' -> 72.25;
                    case 'P' -> 68.25;
                    case 'R' -> 69;
                    case 'S' -> 73.5;
                    case 'V' -> 77;
                    case 'W' -> 107.75;
                    case 'X' -> 76.25;
                    case 'Y' -> 80;
                    case '0' -> 72.25;
                    case '1' -> 40;
                    case '2' -> 69.25;
                    case '3' -> 66;
                    case '4' -> 73.5;
                    case '5' -> 68.25;
                    case '6', '9' -> 69.75;
                    case '7' -> 63;
                    case '8' -> 67;
                    case '-' -> 32.5;
                    case ' ' -> 0;
                    case '\'' -> 0;
                    case '_' -> 0;
                    case '^' -> 0;
                    default -> throw new IllegalStateException("Unexpected value: " + c);
                };
                return base*hc/100;
            }
            case L2serre -> {
                base = switch (c) {
                    case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'X' -> 68;
                    case 'B', '3' -> 61.5;
                    case 'C', '6', '9', '2' -> 63.5;
                    case 'D' -> 59.75;
                    case 'E', 'É', 'È', 'Ê', 'Ë' -> 52.5;
                    case 'F' -> 52.2;
                    case 'G', 'S', 'Q', 'O' -> 65;
                    case 'H', '7' -> 60.5;
                    case 'I', '.' -> 16;
                    case 'J' -> 56;
                    case 'K' -> 60.25;
                    case 'L' -> 48;
                    case 'M' -> 82.5;
                    case 'N' -> 59.25;
                    case 'P' -> 60;
                    case 'R' -> 60;
                    case 'T' -> 53.5;
                    case 'U' -> 60;
                    case 'V' -> 67.5;
                    case 'W' -> 98;
                    case 'Y' -> 71.75;
                    case 'Z' -> 60.25;
                    case '0' -> 65.25;
                    case '1' -> 36;
                    case '4' -> 66.25;
                    case '5' -> 68.75;
                    case '8' -> 64.25;
                    case '-' -> 30;
                    case ' ' -> 18.03 * 4;
                    case '\'' -> 0;
                    default -> throw new IllegalStateException();
                };
                return base * hc / 100;
            }
            case L4serre -> {
                if (Character.isLetter(c)) {
                    if (Character.isUpperCase(c)) {
                        base= switch (c) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä' -> 78.5;
                            case 'B' -> 64;
                            case 'C' -> 72;
                            case 'D' -> 71.25;
                            case 'E', 'É', 'È', 'Ê', 'Ë' -> 52.75;
                            case 'F' -> 51.5;
                            case 'G' -> 75.5;
                            case 'H' -> 65.5;
                            case 'I' -> 17;
                            case 'J' -> 55.5;
                            case 'K' -> 69.5;
                            case 'L' -> 51;
                            case 'M' -> 93;
                            case 'N' -> 67.25;
                            case 'O' -> 74.5;
                            case 'P' -> 63.5;
                            case 'Q' -> 82.5;
                            case 'R' -> 67.25;
                            case 'S', 'T' -> 67;
                            case 'U' -> 66;
                            case 'V' -> 75;
                            case 'W' -> 118;
                            case 'X' -> 81.25;
                            case 'Y' -> 70.5;
                            case 'Z' -> 65;

                            default -> throw new IllegalStateException("Unexpected value: " + c);
                        };
                        return base*hc/100;
                    } else if (Character.isLowerCase(c)) {
                        base= switch (c) {
                            case 'a', 'à', 'á', 'â', 'ã', 'ä' -> 53.25;
                            case 'b', 'd', 'p', 'q' -> 55;
                            case 'c', 'g' -> 54.5;
                            case 'e', 'é', 'è', 'ê', 'ë' -> 55.5;
                            case 'f' -> 37.25;
                            case 'h', 'u' -> 53;
                            case 'i', 'l' -> 16;
                            case 'j' -> 29.25;
                            case 'k' -> 63;
                            case 'm' -> 88.5;
                            case 'n' -> 53.5;
                            case 'o' -> 57.25;
                            case 'r' -> 32;
                            case 's' -> 49;
                            case 't' -> 40.25;
                            case 'v' -> 58.25;
                            case 'w' -> 98.5;
                            case 'x' -> 62;
                            case 'y' -> 59;
                            case 'z' -> 50;

                            default -> throw new IllegalStateException("Unexpected value: " + c);
                        };
                        return base*hc/100;
                    } else if (Character.isDigit(c)) {
                        base= switch (c) {
                            case '0' -> 72;
                            case '1' -> 30.75;
                            case '2' -> 63.55;
                            case '3' -> 68;
                            case '4' -> 72;
                            case '5' -> 69;
                            case '6', '9' -> 70.25;
                            case '7' -> 62.25;
                            case '8' -> 68.25;

                            default -> throw new IllegalStateException("Unexpected value: " + c);
                        };
                        return base*hc/100;
                    } else throw new IllegalStateException("Unexpected value: " + c);
                } else {
                    base= switch (c) {
                        case '-' -> 28;
                        case ' ', '_', '^' -> 0;
                        case '\'' -> 0;
                        case '.' -> 14;
                        case '0' -> 72;
                        case '1' -> 30.75;
                        case '2' -> 63.5;
                        case '3' -> 68;
                        case '4' -> 72;
                        case '5' -> 69;
                        case '6' -> 70.25;
                        case '7' -> 62.25;
                        case '8' -> 68.25;
                        case '9' -> 70.25;
                        case '(', ')' -> 22;
                        default -> throw new IllegalStateException("Unexpected value: " + c);
                    };
                    return base*hc/100;
                }
            }
        }
        return -1;
    }

    public static double getGapLengthBetweenLetter(char c1, char c2, SignFont font, int numeroGamme) {
        int code = 0;
        double hc = font==SignFont.L2serre ?
                DirectionalSign.gammes[numeroGamme] : DirectionalSign.gammes[numeroGamme+1];

        if (c1 == '-' || c2 == '-') return 0.3*hc;
        if (c1 == '.' || c2 == '.') return 0.25*hc;
        if (c1 == ' ' || c2 == ' ') return font==SignFont.L2serre ? 0.25*0.5*(hc-1) : 0.5*0.75*hc;
        if (c1 == '\'') return 0.7*hc;
        else if (c2 == '\'') return 0.1*hc;
        if (c1 == '_' || c2 == '_') return 0.4*hc;
        if (c1 == '^' || c2 == '^') return 0.4*hc;

        if (Character.isDigit(c1) && Character.isDigit(c2)) {
            return getGapLengthBetweenDigits(c1, c2, font);
        } else if ((Character.isDigit(c1) && !Character.isDigit(c2))
                ||(Character.isDigit(c2) && !Character.isDigit(c1))) {
            return 0.5*hc;
        } else if (Character.isLowerCase(c1) && font != SignFont.L4serre) {
            return getGapLengthBetweenLetter(Character.toUpperCase(c1), c2, font, numeroGamme);
        } else if (Character.isLowerCase(c2) && font != SignFont.L4serre) {
            return getGapLengthBetweenLetter(c1, Character.toUpperCase(c2), font, numeroGamme);
        }

        switch (font) {
            case L1serre, L2serre -> {
                switch (c1) {
                    case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä' -> code = switch (c2) {
                        case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'C', 'K', 'L', 'W', 'Z' -> 4;
                        case 'E', 'É', 'È', 'Ê', 'Ë', 'H', 'I', 'M', 'N', 'U', 'J' -> 6;
                        case 'B', 'G', 'O', 'R', 'Q', 'D', 'S' -> 5;
                        case 'V', 'F', 'X' -> 3;
                        case 'T', 'P', 'Y' -> 2;
                        default -> throw new IllegalStateException();
                    };
                    case 'B', 'D', 'E', 'É', 'È', 'Ê', 'Ë', 'F',
                            'H', 'I', 'K', 'L', 'M', 'P', 'R', 'N' -> code = switch (c2) {
                        case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'X', 'V', 'T', 'F', 'C' -> 6;
                        case 'B', 'R', 'E', 'É', 'È', 'Ê', 'Ë', 'Q', 'O', 'G', 'D' -> 8;
                        case 'H', 'P', 'M', 'N', 'U', 'J', 'I', 'L' -> 9;
                        case 'K', 'Z', 'W', 'S' -> 7;
                        case 'Y' -> 5;
                        default -> throw new IllegalStateException("Unexpected value: " + c2);
                    };
                    case 'C', 'G', 'O', 'Q' -> code = switch (c2) {
                        case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'C', 'F', 'K', 'P', 'T', 'V', 'X' -> 5;
                        case 'B', 'Q', 'O', 'G', 'D', 'U', 'J', 'R' -> 7;
                        case 'E', 'É', 'È', 'Ê', 'Ë', 'S', 'W', 'Z' -> 6;
                        case 'H', 'I', 'M', 'N' -> 8;
                        case 'L', 'Y' -> 4;
                        default -> throw new IllegalStateException();
                    };
                    case 'J' -> code = switch (c2) {
                        case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'C', 'L', 'X', 'Z' -> 3;
                        case 'B', 'H', 'I', 'M', 'N' -> 5;
                        case 'Q', 'O', 'G', 'D', 'K', 'R', 'S' -> 4;
                        case 'E', 'É', 'È', 'Ê', 'Ë', 'U', 'J' -> 6;
                        case 'F', 'P', 'W' -> 2;
                        case 'T', 'V', 'Y' -> 1;
                        default -> throw new IllegalStateException("Unexpected value: " + c2);
                    };
                    case 'S' -> code = switch (c2) {
                        case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'L', 'P', 'T', 'X' -> 4;
                        case 'B', 'Q', 'O', 'G', 'D', 'U', 'J', 'R' -> 6;
                        case 'C', 'E', 'É', 'È', 'Ê', 'Ë', 'F', 'K', 'S', 'V', 'W', 'Z' -> 5;
                        case 'H', 'I', 'M', 'N' -> 7;
                        case 'Y' -> 2;
                        default -> throw new IllegalStateException("Unexpected value: " + c2);
                    };
                    case 'T' -> code = switch (c2) {
                        case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'L' -> 2;
                        case 'B', 'C', 'Q', 'O', 'G', 'D', 'F', 'K', 'R', 'W', 'Z' -> 5;
                        case 'E', 'É', 'È', 'Ê', 'Ë', 'H', 'I', 'M', 'N', 'U', 'J' -> 6;
                        case 'P', 'Y' -> 3;
                        case 'S', 'T', 'V', 'X' -> 4;
                        default -> throw new IllegalStateException("Unexpected value: " + c2);
                    };
                    case 'U' -> code = switch (c2) {
                        case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'C', 'F', 'K', 'T', 'V', 'Z' -> 6;
                        case 'B', 'Q', 'O', 'G', 'D', 'R', 'S', 'W' -> 7;
                        case 'E', 'É', 'È', 'Ê', 'Ë', 'U', 'J' -> 8;
                        case 'H', 'I', 'M', 'N' -> 9;
                        case 'L', 'P', 'X' -> 5;
                        case 'Y' -> 3;
                        default -> throw new IllegalStateException();
                    };
                    case 'V' -> code = switch (c2) {
                        case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'P' -> 3;
                        case 'B', 'C', 'Q', 'O', 'G', 'D', 'F', 'K', 'S', 'T', 'X' -> 4;
                        case 'E', 'É', 'È', 'Ê', 'Ë', 'R', 'V', 'W', 'Z' -> 5;
                        case 'H', 'I', 'M', 'N', 'U', 'J' -> 6;
                        case 'L', 'Y' -> 2;
                        default -> throw new IllegalStateException();
                    };
                    case 'W' -> code = switch (c2) {
                        case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'C', 'P', 'Y' -> 4;
                        case 'B', 'Q', 'O', 'G', 'D', 'E', 'É', 'È', 'Ê', 'Ë', 'U', 'J', 'R', 'S' -> 6;
                        case 'F', 'K', 'T', 'W', 'V', 'X', 'Z' -> 5;
                        case 'H', 'I', 'M', 'N' -> 7;
                        case 'L' -> 3;
                        default -> throw new IllegalStateException();
                    };
                    case 'X' -> code = switch (c2) {
                        case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'P', 'S', 'X', 'Y' -> 3;
                        case 'B', 'C', 'F', 'K', 'T', 'V' -> 4;
                        case 'Q', 'O', 'G', 'D', 'E', 'É', 'È', 'Ê', 'Ë', 'U', 'J', 'R', 'W', 'Z' -> 5;
                        case 'H', 'I', 'M', 'N' -> 6;
                        case 'L' -> 2;
                        default -> throw new IllegalStateException("Unexpected value: " + c2);
                    };
                    case 'Y' -> code = switch (c2) {
                        case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'C', 'P', 'V' -> 2;
                        case 'B', 'Q', 'O', 'G', 'D', 'T', 'X' -> 3;
                        case 'E', 'É', 'È', 'Ê', 'Ë', 'F', 'U', 'J', 'K', 'R', 'S', 'W', 'Z' -> 4;
                        case 'H', 'I', 'M', 'N' -> 5;
                        case 'L', 'Y' -> 1;
                        default -> throw new IllegalStateException();
                    };
                    case 'Z' -> code = switch (c2) {
                        case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'P' -> 4;
                        case 'B', 'T', 'S', 'K' -> 6;
                        case 'C', 'X', 'W', 'V', 'R', 'L', 'F', 'Q', 'O', 'G', 'D' -> 5;
                        case 'E', 'É', 'È', 'Ê', 'Ë', 'U', 'J', 'H', 'I', 'M', 'N' -> 7;
                        case 'Y', 'Z' -> 3;
                        default -> throw new IllegalStateException();
                    };
                    default -> throw new IllegalStateException();
                }

                switch (font) {
                    case L1serre -> {
                        return switch (code) {
                            case 1 -> 10*hc/100;
                            case 2 -> 14.5*hc/100;
                            case 3 -> 19.5*hc/100;
                            case 4 -> 22.5*hc/100;
                            case 5 -> 26.5*hc/100;
                            case 6 -> 30.5*hc/100;
                            case 7 -> 34*hc/100;
                            case 8 -> 37.5*hc/100;
                            case 9 -> 42*hc/100;
                            default -> throw new IllegalStateException("Unexpected code: " + code);
                        };
                    }
                    case L2serre -> {
                        return switch (code) {
                            case 1 -> 9*hc/100;
                            case 2 -> 12.5*hc/100;
                            case 3 -> 16*hc/100;
                            case 4 -> 19.5*hc/100;
                            case 5 -> 23*hc/100;
                            case 6 -> 26.5*hc/100;
                            case 7 -> 30*hc/100;
                            case 8 -> 32.5*hc/100;
                            case 9 -> 37*hc/100;
                            default -> throw new IllegalStateException("Unexpected code: " + code);
                        };
                    }
                    default -> throw new IllegalStateException();
                }

            }
            case L4serre -> {/* @TODO taille des espaces entre majuscules et minuscules à faire*/
                if ( (Character.isUpperCase(c1)) && (Character.isUpperCase(c2)) ) {
                    switch (c1) {
                        case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'C', 'K', 'L', 'W', 'Z' -> 4;
                            case 'E', 'É', 'È', 'Ê', 'Ë', 'U', 'J' -> 6;
                            case 'B', 'O', 'Q', 'G', 'D', 'H', 'I', 'M', 'N', 'R', 'S', 'V' -> 5;
                            case 'F', 'X' -> 3;
                            case 'P', 'T', 'Y' -> 2;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'B', 'D', 'E', 'É', 'È', 'Ê', 'Ë', 'F',
                                'H', 'K', 'L', 'M', 'P', 'R', 'N' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'X', 'V', 'C', 'P' -> 6;
                            case 'B', 'R', 'E', 'É', 'È', 'Ê', 'Ë', 'Q', 'G', 'D', 'L' -> 8;
                            case 'H', 'M', 'N', 'U', 'J', 'O', 'I', 'T' -> 9;
                            case 'K', 'Z', 'W', 'S' -> 7;
                            case 'F', 'Y' -> 5;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'I' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'X', 'V', 'C', 'P' -> 6;
                            case 'B', 'R', 'E', 'É', 'È', 'Ê', 'Ë', 'Q', 'G', 'D' -> 8;
                            case 'H', 'M', 'N', 'U', 'J', 'O', 'I', 'T', 'L' -> 9;
                            case 'K', 'Z', 'W', 'S' -> 7;
                            case 'F', 'Y' -> 5;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'C', 'G', 'O', 'Q' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'C', 'F', 'K', 'P', 'T', 'V', 'X' -> 5;
                            case 'B', 'Q', 'O', 'G', 'D', 'U', 'J', 'R' -> 7;
                            case 'E', 'É', 'È', 'Ê', 'Ë', 'S', 'W', 'Z', 'N' -> 6;
                            case 'H', 'I', 'M' -> 8;
                            case 'L', 'Y' -> 4;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'J' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'C', 'L', 'X', 'Z' -> 3;
                            case 'B', 'H', 'I', 'M', 'N' -> 5;
                            case 'Q', 'O', 'G', 'D', 'K', 'R', 'S' -> 4;
                            case 'E', 'É', 'È', 'Ê', 'Ë', 'U', 'J' -> 6;
                            case 'F', 'P', 'W' -> 2;
                            case 'T', 'V', 'Y' -> 1;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'S' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'L', 'P', 'T', 'X' -> 4;
                            case 'B', 'Q', 'O', 'G', 'D', 'U', 'J', 'R' -> 6;
                            case 'C', 'E', 'É', 'È', 'Ê', 'Ë', 'F', 'K', 'S', 'V', 'W', 'Z' -> 5;
                            case 'H', 'I', 'M', 'N' -> 7;
                            case 'Y' -> 2;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'T' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'L', 'R' -> 2;
                            case 'B', 'C', 'Q', 'O', 'G', 'D', 'F', 'K', 'W', 'Z' -> 5;
                            case 'E', 'É', 'È', 'Ê', 'Ë', 'H', 'M', 'N', 'U', 'J' -> 6;
                            case 'P', 'Y' -> 3;
                            case 'S', 'T', 'V', 'X', 'I' -> 4;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'U' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'C', 'F', 'K', 'T', 'V', 'Z' -> 6;
                            case 'B', 'Q', 'O', 'G', 'D', 'R', 'S', 'W' -> 7;
                            case 'E', 'É', 'È', 'Ê', 'Ë', 'U', 'J' -> 8;
                            case 'H', 'I', 'M', 'N' -> 9;
                            case 'L', 'P', 'X' -> 5;
                            case 'Y' -> 3;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'V' -> code = switch (c2) {
                            case 'P', 'L', 'Y' -> 2;
                            case 'B', 'C', 'Q', 'O', 'G', 'D', 'F', 'K', 'S', 'T', 'X' -> 4;
                            case 'E', 'É', 'È', 'Ê', 'Ë', 'R', 'V', 'W', 'Z' -> 5;
                            case 'H', 'M', 'N', 'U', 'J' -> 6;
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä' -> 0;
                            case 'I' -> 1;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'W' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'C', 'P', 'Y' -> 4;
                            case 'B', 'Q', 'O', 'G', 'D', 'E', 'É', 'È', 'Ê', 'Ë', 'U', 'J', 'R', 'S' -> 6;
                            case 'F', 'K', 'T', 'W', 'V', 'X', 'Z' -> 5;
                            case 'H', 'I', 'M', 'N' -> 7;
                            case 'L' -> 3;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'X' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'P', 'S', 'X', 'Y' -> 3;
                            case 'B', 'C', 'F', 'K', 'T', 'V' -> 4;
                            case 'Q', 'O', 'G', 'D', 'E', 'É', 'È', 'Ê', 'Ë', 'U', 'J', 'R', 'W', 'Z' -> 5;
                            case 'H', 'I', 'M', 'N' -> 6;
                            case 'L' -> 2;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'Y' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'C', 'P', 'V' -> 2;
                            case 'B', 'Q', 'O', 'G', 'D', 'T', 'X' -> 3;
                            case 'E', 'É', 'È', 'Ê', 'Ë', 'F', 'U', 'J', 'K', 'R', 'S', 'W', 'Z' -> 4;
                            case 'H', 'I', 'M', 'N' -> 5;
                            case 'L', 'Y' -> 1;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'Z' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'P' -> 4;
                            case 'B', 'T', 'S', 'K' -> 6;
                            case 'C', 'X', 'W', 'V', 'R', 'L', 'F', 'Q', 'O', 'G', 'D' -> 5;
                            case 'E', 'É', 'È', 'Ê', 'Ë', 'U', 'J', 'H', 'I', 'M', 'N' -> 7;
                            case 'Y', 'Z' -> 3;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        default -> throw new IllegalStateException("Unexpected value: " + c1);
                    }

                    return switch (code) {
                        case 1 -> 8*hc/100;
                        case 2 -> 11*hc/100;
                        case 3 -> 14*hc/100;
                        case 4 -> 17*hc/100;
                        case 5 -> 20*hc/100;
                        case 6 -> 23*hc/100;
                        case 7 -> 26*hc/100;
                        case 8 -> 28*hc/100;
                        case 9 -> 31*hc/100;
                        default -> -1;
                    };
                } else if ( (Character.isLowerCase(c1)) && (Character.isLowerCase(c2)) ) {
                    c2 = Character.toUpperCase(c2);
                    switch (Character.toUpperCase(c1)) {
                        case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'H', 'M', 'N', 'B', 'P', 'E', 'É', 'È', 'Ê', 'Ë', 'O', 'C' -> 6;
                            case 'D', 'G', 'I', 'J', 'L', 'Q', 'U' -> 7;
                            case 'F', 'S', 'T', 'Z' -> 5;
                            case 'K', 'X' -> 3;
                            case 'R', 'Y', 'V', 'W' -> 4;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'B', 'D', 'E', 'É', 'È', 'Ê', 'Ë', 'F',
                                'H', 'K', 'L', 'M', 'P', 'R', 'N', 'I' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'H', 'M', 'N' -> 8;
                            case 'B', 'P', 'E', 'É', 'È', 'Ê', 'Ë', 'O' -> 7;
                            case 'C' -> 7;
                            case 'D', 'G', 'I', 'J', 'L', 'Q', 'U' -> 9;
                            case 'F' -> 6;
                            case 'K' -> 4;
                            case 'R' -> 6;
                            case 'S' -> 6;
                            case 'T' -> 6;
                            case 'Y', 'V', 'W' -> 4;
                            case 'X' -> 4;
                            case 'Z' -> 6;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'C', 'G', 'O', 'Q' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'H', 'M', 'N', 'B', 'P', 'E', 'É', 'È', 'Ê', 'Ë', 'O' -> 6;
                            case 'C', 'S', 'T' -> 5;
                            case 'D', 'G', 'I', 'J', 'L', 'Q', 'U' -> 7;
                            case 'F', 'R', 'Y', 'V', 'W', 'Z' -> 4;
                            case 'K', 'X' -> 3;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'J' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'H', 'M', 'N' -> 4;
                            case 'B', 'P', 'E', 'É', 'È', 'Ê', 'Ë', 'O', 'C', 'R', 'S', 'T', 'Z' -> 3;
                            case 'D', 'G', 'I', 'J', 'L', 'Q', 'U' -> 5;
                            case 'F', 'K' -> 0;
                            case 'Y', 'V', 'W', 'X' -> 2;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'S' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'H', 'M', 'N', 'D', 'G', 'I', 'J', 'L', 'Q', 'U' -> 7;
                            case 'B', 'P', 'E', 'É', 'È', 'Ê', 'Ë', 'O' -> 5;
                            case 'C', 'R', 'S', 'T', 'Y', 'V', 'W', 'Z' -> 4;
                            case 'F', 'K' -> 3;
                            case 'X' -> 2;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'T' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'H', 'M', 'N', 'B', 'P', 'E', 'É', 'È', 'Ê', 'Ë', 'O', 'C', 'R', 'S', 'Y', 'V', 'W', 'Z' -> 4;
                            case 'D', 'G', 'I', 'J', 'L', 'Q', 'U' -> 6;
                            case 'F', 'T', 'X' -> 3;
                            case 'K' -> 2;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'U' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'H', 'M', 'N', 'D', 'G', 'I', 'J', 'L', 'Q', 'U' -> 7;
                            case 'B', 'P', 'E', 'É', 'È', 'Ê', 'Ë', 'O', 'C', 'R', 'X' -> 6;
                            case 'F', 'S', 'T', 'Z' -> 5;
                            case 'K', 'Y', 'V', 'W' -> 4;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'V', 'W', 'Y' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'H', 'M', 'N', 'D', 'G', 'I', 'J', 'L', 'Q', 'U' -> 6;
                            case 'B', 'P', 'E', 'É', 'È', 'Ê', 'Ë', 'O', 'C', 'S', 'T' -> 4;
                            case 'F', 'R', 'Z' -> 3;
                            case 'K', 'Y', 'V', 'W', 'X' -> 2;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'X' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'H', 'M', 'N', 'D', 'G', 'I', 'J', 'L', 'Q', 'U' -> 4;
                            case 'B', 'P', 'E', 'É', 'È', 'Ê', 'Ë', 'O', 'C', 'R' -> 3;
                            case 'F' -> 1;
                            case 'K' -> 0;
                            case 'S', 'T', 'Y', 'V', 'W', 'X', 'Z' -> 2;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'Z' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'H', 'M', 'N', 'D', 'G', 'I', 'J', 'L', 'Q', 'U' -> 6;
                            case 'B', 'P', 'E', 'É', 'È', 'Ê', 'Ë', 'O', 'C', 'R' -> 4;
                            case 'F', 'K', 'Y', 'V', 'W', 'Z' -> 3;
                            case 'S', 'T', 'X' -> 5;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        default -> throw new IllegalStateException("Unexpected value: " + c1);
                    }
                    return switch (code) {
                        case 0 -> 0.8*hc/100;
                        case 1 -> 10*hc/100;
                        case 2 -> 13*hc/100;
                        case 3 -> 17*hc/100;
                        case 4 -> 20*hc/100;
                        case 5 -> 23*hc/100;
                        case 6 -> 25*hc/100;
                        case 7 -> 27*hc/100;
                        case 8 -> 30*hc/100;
                        case 9 -> 33*hc/100;
                        default -> throw new IllegalStateException("Unexpected code: " + code);
                    };
                } else if ( (Character.isLowerCase(c1) && Character.isUpperCase(c2))
                        || (Character.isUpperCase(c1) && Character.isLowerCase(c2)) ) {
                    c2 = Character.toUpperCase(c2);
                    switch (Character.toUpperCase(c1)) {
                        case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'E', 'É', 'È', 'Ê', 'Ë', 'R', 'S' -> 6;
                            case 'B', 'C', 'D', 'F', 'H', 'I', 'M', 'N', 'U', 'J' -> 7;
                            case 'O', 'Ô', 'G' -> 8;
                            case 'X', 'K', 'V', 'W', 'Y' -> 2;
                            case 'L', 'Q', 'P', 'Z' -> 4;
                            case 'T' -> 1;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'B', 'H', 'I', 'K', 'L', 'M', 'N', 'P', 'R' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'V', 'W' -> 5;
                            case 'B', 'C', 'D', 'E', 'É', 'È', 'Ê', 'Ë', 'F', 'O', 'Ô', 'G', 'U', 'J' -> 8;
                            case 'H', 'I', 'M', 'N' -> 9;
                            case 'X', 'K' -> 3;
                            case 'L', 'Q', 'P' -> 4;
                            case 'R', 'S', 'Z' -> 6;
                            case 'T', 'Y' -> 2;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'C', 'D', 'E', 'É', 'È', 'Ê', 'Ë', 'O', 'Ô', 'Q' -> code = switch (c2) {
                            case 'A', 'À', 'Á', 'Â', 'Ã', 'Ä', 'L', 'Q', 'P', 'R', 'Z' -> 4;
                            case 'B', 'E', 'É', 'È', 'Ê', 'Ë', 'O', 'Ô', 'G', 'H', 'I', 'M', 'N', 'U', 'J' -> 7;
                            case 'C', 'D', 'F', 'S' -> 6;
                            case 'X', 'K' -> 1;
                            case 'T' -> 0;
                            case 'V', 'W', 'Y' -> 2;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'F' -> code = switch (c2) {
                            case 'A', 'Q', 'P', 'V', 'W' -> 3;
                            case 'B', 'O', 'Ô', 'G', 'U', 'J', 'S' -> 5;
                            case 'C', 'D', 'F', 'R', 'Z' -> 4;
                            case 'E', 'H', 'I', 'M', 'N' -> 6;
                            case 'X', 'K', 'T' -> 0;
                            case 'L' -> 2;
                            case 'Y' -> 1;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'G' -> code = switch (c2) {
                            case 'A', 'Q', 'P', 'R', 'Z' -> 4;
                            case 'B', 'E', 'O', 'Ô', 'G', 'U', 'J' -> 7;
                            case 'C', 'D', 'F', 'S' -> 6;
                            case 'H', 'I', 'M', 'N' -> 8;
                            case 'X', 'K' -> 1;
                            case 'L' -> 3;
                            case 'T' -> 0;
                            case 'V', 'W', 'Y' -> 2;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'J' -> code = switch (c2) {
                            case 'A', 'C', 'D', 'O', 'Ô', 'G', 'R', 'S' -> 3;
                            case 'B', 'F', 'H', 'I', 'M', 'N', 'U', 'J' -> 4;
                            case 'E' -> 6;
                            case 'X', 'K', 'T', 'V', 'W', 'Y' -> 0;
                            case 'L', 'Q', 'Z' -> 2;
                            case 'P' -> 1;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'S' -> code = switch (c2) {
                            case 'A', 'L', 'P' -> 3;
                            case 'B', 'D', 'E', 'O', 'Ô', 'G', 'H', 'I', 'M', 'N' -> 6;
                            case 'C', 'U', 'J', 'S' -> 5;
                            case 'F', 'Q', 'R' -> 4;
                            case 'X', 'K', 'T', 'Y' -> 0;
                            case 'V', 'W', 'Z' -> 2;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'T' -> code = switch (c2) {
                            case 'A', 'V', 'W' -> 1;
                            case 'B', 'P', 'R' -> 3;
                            case 'C', 'D', 'F', 'O', 'Ô', 'G', 'U', 'J', 'S' -> 4;
                            case 'E' -> 6;
                            case 'H', 'I', 'M', 'N' -> 5;
                            case 'X', 'K', 'L', 'T', 'Y' -> 0;
                            case 'Q', 'Z' -> 2;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'U' -> code = switch (c2) {
                            case 'A' -> 4;
                            case 'B', 'F', 'S' -> 7;
                            case 'C', 'D', 'E', 'O', 'Ô', 'G', 'U', 'J' -> 8;
                            case 'H', 'I', 'M', 'N' -> 9;
                            case 'X', 'K', 'T', 'Y' -> 2;
                            case 'L' -> 3;
                            case 'Q', 'P', 'V', 'W', 'Z' -> 5;
                            case 'R' -> 6;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'V', 'W', 'Y' -> code = switch (c2) {
                            case 'A', 'V', 'W' -> 1;
                            case 'B', 'C', 'D', 'E', 'O', 'Ô', 'G', 'U', 'J', 'S' -> 4;
                            case 'F', 'Q', 'P', 'R', 'Z' -> 3;
                            case 'H', 'I', 'M', 'N' -> 5;
                            case 'X', 'K', 'L', 'T', 'Y' -> 0;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'X' -> code = switch (c2) {
                            case 'A', 'Q', 'P' -> 2;
                            case 'B', 'C', 'D', 'R', 'S', 'Z' -> 3;
                            case 'E' -> 5;
                            case 'F', 'O', 'Ô', 'G', 'H', 'I', 'M', 'N', 'U', 'J' -> 4;
                            case 'X', 'K', 'Y' -> 0;
                            case 'L', 'T', 'V', 'W' -> 1;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };
                        case 'Z' -> code = switch (c2) {
                            case 'A' -> 4;
                            case 'B' -> 3;
                            case 'C' -> 5;
                            case 'D' -> 5;
                            case 'E' -> 6;
                            case 'F' -> 4;
                            case 'O', 'Ô', 'G' -> 5;
                            case 'H', 'I', 'M', 'N' -> 6;
                            case 'U', 'J' -> 6;
                            case 'X', 'K' -> 0;
                            case 'L' -> 3;
                            case 'Q' -> 4;
                            case 'P' -> 4;
                            case 'R' -> 4;
                            case 'S' -> 4;
                            case 'T' -> 1;
                            case 'V', 'W' -> 3;
                            case 'Y' -> 1;
                            case 'Z' -> 3;
                            default -> throw new IllegalStateException("Unexpected value: " + c2);
                        };

                        default -> throw new IllegalStateException("Unexpected value: " + c1);
                    }
                    return switch (code) {
                        case 0 -> 0.8;
                        case 1 -> 10;
                        case 2 -> 13;
                        case 3 -> 17;
                        case 4 -> 20;
                        case 5 -> 23;
                        case 6 -> 25;
                        case 7 -> 27;
                        case 8 -> 30;
                        case 9 -> 33;
                        default -> throw new IllegalStateException("Unexpected code: " + code);
                    };
                } else {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(),
                            "c1: " + c1 + " c2: " + c2 +
                                    " - les dimensions d'espaces entre ces caractères n'ont pas encore été definies");
                }


            }
        }
        JOptionPane.showMessageDialog(JOptionPane.getRootFrame(),
                "c1: " + c1 + " c2: " + c2 + " - cette dimension d'espace n'est pas definie");
        return -1.0;
    }

    public static double getGapLengthBetweenDigits(char c1, char c2, SignFont font) {
        int code = 1;
        switch (font) {
            case L1serre, L2serre -> {
                switch (c1) {
                    case '0', '6' -> code = switch (c2) {
                        case '0', '9' -> 7;
                        case '1' -> 8;
                        case '2', '4', '6', '3', '5', '8' -> 6;
                        case '7' -> 5;
                        default -> throw new IllegalStateException("Unexpected value: " + c2);
                    };
                    case '1' -> code = switch (c2) {
                        case '0', '9', '2', '4', '6', '3', '8' -> 6;
                        case '1' -> 7;
                        case '5', '7' -> 5;
                        default -> throw new IllegalStateException("Unexpected value: " + c2);
                    };
                    case '2', '5' -> code = switch (c2) {
                        case '0', '9', '2', '4', '6', '3', '5' -> 6;
                        case '1' -> 8;
                        case '7', '8' -> 5;
                        default -> throw new IllegalStateException("Unexpected value: " + c2);
                    };
                    case '3' -> code = switch (c2) {
                        case '0', '9', '2', '4', '6', '3' -> 5;
                        case '1' -> 7;
                        case '5', '7', '8' -> 4;
                        default -> throw new IllegalStateException("Unexpected value: " + c2);
                    };
                    case '4' -> code = switch (c2) {
                        case '0', '9', '1' -> 6;
                        case '2', '4', '6', '3', '7' -> 5;
                        case '5', '8' -> 4;
                        default -> throw new IllegalStateException("Unexpected value: " + c2);
                    };
                    case '7' -> code = switch (c2) {
                        case '0', '9', '2', '4', '6', '3', '8' -> 5;
                        case '1' -> 7;
                        case '5', '7' -> 4;
                        default -> throw new IllegalStateException("Unexpected value: " + c2);
                    };
                    case '8' -> code = switch (c2) {
                        case '0', '9', '2', '4', '6', '5' -> 6;
                        case '1' -> 8;
                        case '3', '8' -> 7;
                        case '7' -> 5;
                        default -> throw new IllegalStateException("Unexpected value: " + c2);
                    };
                    case '9' -> code = switch (c2) {
                        case '0', '9', '2', '4', '6', '3', '5', '8' -> 6;
                        case '1' -> 8;
                        case '7' -> 5;
                        default -> throw new IllegalStateException("Unexpected value: " + c2);
                    };
                    default -> throw new IllegalStateException("Unexpected value: " + c1);
                }
            }
            case L4serre -> {
                switch (c1) {
                    case '0', '6' -> code = switch (c2) {
                        case '0', '9' -> 7;
                        case '1' -> 8;
                        case '2', '4', '6', '3', '5', '8' -> 6;
                        case '7' -> 5;
                        default -> throw new IllegalStateException("Unexpected value: " + c2);
                    };
                    case '1' -> code = switch (c2) {
                        case '0', '9', '2', '4', '6', '3', '8' -> 6;
                        case '1' -> 7;
                        case '5', '7' -> 5;
                        default -> throw new IllegalStateException("Unexpected value: " + c2);
                    };
                    case '2', '5' -> code = switch (c2) {
                        case '0', '9', '2', '4', '6', '3', '5' -> 6;
                        case '1' -> 8;
                        case '7', '8' -> 5;
                        default -> throw new IllegalStateException("Unexpected value: " + c2);
                    };
                    case '3' -> code = switch (c2) {
                        case '0', '9', '2', '4', '6', '3' -> 5;
                        case '1' -> 7;
                        case '5', '7', '8' -> 4;
                        default -> throw new IllegalStateException("Unexpected value: " + c2);
                    };
                    case '4' -> code = switch (c2) {
                        case '0', '9', '1' -> 6;
                        case '2', '4', '6', '3', '7' -> 5;
                        case '5', '8' -> 4;
                        default -> throw new IllegalStateException("Unexpected value: " + c2);
                    };
                    case '7' -> code = switch (c2) {
                        case '0', '9', '2', '4', '6', '3', '8' -> 5;
                        case '1' -> 7;
                        case '5', '7' -> 4;
                        default -> throw new IllegalStateException("Unexpected value: " + c2);
                    };
                    case '8' -> code = switch (c2) {
                        case '0', '9', '2', '4', '6', '5' -> 6;
                        case '1' -> 8;
                        case '3', '8' -> 7;
                        case '7' -> 5;
                        default -> throw new IllegalStateException("Unexpected value: " + c2);
                    };
                    case '9' -> code = switch (c2) {
                        case '0', '9', '2', '4', '6', '3', '5', '8' -> 6;
                        case '1' -> 8;
                        case '7' -> 5;
                        default -> throw new IllegalStateException("Unexpected value: " + c2);
                    };
                    default -> throw new IllegalStateException("Unexpected value: " + c1);
                }
            }
        }

        switch (font) {
            case L1serre -> {
                return switch (code) {
                    case 4 -> 22.5;
                    case 5 -> 26.5;
                    case 6 -> 30.5;
                    case 7 -> 34;
                    case 8 -> 37.5;
                    default -> throw new IllegalStateException();
                };
            }

            case L2serre -> {
                return switch (code) {
                    case 4 -> 19.5;
                    case 5 -> 23;
                    case 6 -> 26.5;
                    case 7 -> 30;
                    case 8 -> 32.5;
                    default -> throw new IllegalStateException();
                };
            }

            case L4serre -> {
                return switch (code) {
                    case 4 -> 17;
                    case 5 -> 20;
                    case 6 -> 23;
                    case 7 -> 26;
                    case 8 -> 28;
                    default -> throw new IllegalStateException();
                };
            }
            default -> throw new IllegalStateException("Unexpected value: " + font);
        }
    }
}
