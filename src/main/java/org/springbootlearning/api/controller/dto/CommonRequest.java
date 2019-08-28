package org.springbootlearning.api.controller.dto;

public class CommonRequest {

    private Long userId;
    private Long nonce;
    
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getNonce() {
        return nonce;
    }
    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }
    
    @Override
    public String toString() {
        return "CommonRequest [userId=" + userId + ", nonce=" + nonce + "]";
    }
    
}
