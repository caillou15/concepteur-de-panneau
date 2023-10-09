package fr.sarainfras.caillou15.app.sign;

import org.apache.commons.lang3.NotImplementedException;

import javax.swing.*;

public class Font {
    public enum SignFont {
        // appelé serre poour les polices que j'ai modifiées pour que les espaces soient corrects
        L1serre, L2serre, L4;

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public static double getTextLength(String text, SignFont font, int numeroGamme) {
        int hc = font == SignFont.L2serre ? DirectionalSign.gammes[numeroGamme+1] : DirectionalSign.gammes[numeroGamme];

        if (text.length() == 0) {return 0;}
        else {
            int totalLength = 0;
            char[] charArray = text.toUpperCase().toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                totalLength += getLetterLength(charArray[i], font, hc);
                if (i != charArray.length - 1) totalLength
                        += getGapLengthBetweenLetter(charArray[i], charArray[i+1], font, numeroGamme);
            }
            return totalLength;
        }

    }

    public static double getLetterLength(char c, SignFont font, double hc) {
        double base = -1;
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
                    default -> throw new IllegalStateException();
                };
                return base * hc / 100;
            }
            case L4 -> /* @TODO taille des lettres L4 à faire*/ throw new NotImplementedException("police L4 non implémentée");
        }
        return -1;
    }

    public static double getGapLengthBetweenLetter(char c1, char c2, SignFont font, int numeroGamme) {
        int code = 0;
        double hc = font==SignFont.L2serre ?
                DirectionalSign.gammes[numeroGamme] : DirectionalSign.gammes[numeroGamme+1];

        if (c1 == '-' || c2 == '-') return 0.3*hc;
        if (c1 == '.' || c2 == '.') return 0.25*hc;
        if (c1 == ' ' || c2 == ' ') return font==SignFont.L2serre ? 0.25*hc : 0.5*hc;

        if (Character.isDigit(c1) && Character.isDigit(c2)) {
            return getGapLengthBetweenDigits(c1, c2, font);
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
                            case 1 -> 10;
                            case 2 -> 14.5;
                            case 3 -> 19.5;
                            case 4 -> 22.5;
                            case 5 -> 26.5;
                            case 6 -> 30.5;
                            case 7 -> 34;
                            case 8 -> 37.5;
                            case 9 -> 42;
                            default -> -1;
                        };
                    }
                    case L2serre -> {
                        return switch (code) {
                            case 1 -> 9;
                            case 2 -> 12.5;
                            case 3 -> 16;
                            case 4 -> 19.5;
                            case 5 -> 23;
                            case 6 -> 26.5;
                            case 7 -> 30;
                            case 8 -> 32.5;
                            case 9 -> 37;
                            default -> -1;
                        };
                    }
                    default -> throw new IllegalStateException();
                }

            }
            case L4 -> {/* @TODO taille des espaces entre lettres L4 à faire*/}
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
            case L4 -> throw new NotImplementedException();
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
            default -> throw new IllegalStateException("Unexpected value: " + font);
        }
    }
}
