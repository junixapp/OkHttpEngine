# OkHttpEngine
A simple wrapper for OkHttp!

# Feature
- fluent api
- get/post request
- multipart body

# Usage
- get请求

		 OkHttpEngine.create()
                    .get("url", new OkHttpEngine.OkHttpCallback() {
                        @Override
                        public void onSuccess(String result) {
                            
                        }
                        @Override
                        public void onFail(IOException e) {

                        }
                    });

- post请求

		PostParams params = new PostParams();
        params.add("key","value");
        params.add("file",new File("a.jpg"));
        OkHttpEngine.create()
                   .post("url", params, new OkHttpEngine.OkHttpCallback() {
                       @Override
                       public void onSuccess(String result) {

                       }

                       @Override
                       public void onFail(IOException e) {

                       }
                   });

# Depedency
[![](https://jitpack.io/v/li-xiaojun/OkHttpEngine.svg)](https://jitpack.io/#li-xiaojun/OkHttpEngine)

## Gradle
1. Add it in your root build.gradle at the end of repositories:

		allprojects {
			repositories {
				...
				maven { url "https://jitpack.io" }
			}
		}

2. Add the dependency

		dependencies {
		        compile 'com.github.li-xiaojun:OkHttpEngine:1.0'
		}

	