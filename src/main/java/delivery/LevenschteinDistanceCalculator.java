package delivery;

public class LevenschteinDistanceCalculator implements DistanceCalculator {
    @Override
    public double calculateDistance(String address1, String address2) {
        int maxLength = Math.max(address1.length(), address2.length());
        if (maxLength == 0) {
            return 0.0;
        }

        int[][] dp = new int[address1.length() + 1][address2.length() + 1];

        for (int i = 0; i <= address1.length(); i++) {
            for (int j = 0; j <= address2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (address1.charAt(i - 1) == address2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j], Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
                }
            }
        }

        return (dp[address1.length()][address2.length()] * 100.0) / maxLength;
    }
    
}
