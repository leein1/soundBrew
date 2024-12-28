package com.soundbrew.soundbrew.domain.user;

public enum SubscriptionType {

    BASIC("basic", 10000, 50),
    PREMIUM("premium", 20000, 100),
    VIP("vip", 30000, 200);

    private final String subscriptionName;
    private final int subscriptionPrice;
    private final int creditPerMonth;

    SubscriptionType(String subscriptionName, int subscriptionPrice, int creditPerMonth) {
        this.subscriptionName = subscriptionName;
        this.subscriptionPrice = subscriptionPrice;
        this.creditPerMonth = creditPerMonth;
    }

    public int getCreditPerMonth() {
        return creditPerMonth;
    }

    public int getSubscriptionPrice() {
        return subscriptionPrice;
    }

    public String getSubscriptionName() {
        return subscriptionName;
    }
}
