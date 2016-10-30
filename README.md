# OkHttpEngine
A simple wrapper for OkHttp!

# Feature
- fluent api
- get/post request
- multipart body
- support parse json to pojo

# Usage
- get请求

		 OkHttpEngine.create()
                    .get("url", new OkHttpCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            
                        }
                        @Override
                        public void onFail(IOException e) {

                        }
                    });

- post请求

		PostParams params = new PostParams();
        params.add("key","value")
              .add("file",new File("path"));
        OkHttpEngine.create()
			        .post("url",params,new OkHttpCallback<String>(){
            @Override
            public void onSuccess(String result) {
			}
            @Override
            public void onFail(IOException e) {
			}
        });

- parse json to pojo

		OkHttpEngine.create()
			        .post("url",params,new OkHttpCallback<Bean>(){
            @Override
            public void onSuccess(Bean result) {
			}
            @Override
            public void onFail(IOException e) {
			}
        });


- add header

		OkHttpEngine.create()
                    .addHeader("a","b")
                    .addHeader("a","b")
                    .get("url",null);

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
		        compile 'com.github.li-xiaojun:OkHttpEngine:1.2'
		}

	