module.exports = {
    devServer: {
        proxy: {
            "/": {
                credentials: true,
                target: "http://localhost:8080",
                changeOrigin: true
            },
            auth:{
                token: ''
            }
        }
    }
};