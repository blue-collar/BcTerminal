package com.telewave.battlecommand.directboard.bean;

import java.util.List;

public class AllLiveBean {

    /**
     * id : e23a1005fe7b45c5ade1d029d8e3ed06
     * name : 江西总队
     * pid : 1
     * pname : 消防局
     * category : 02
     * roomNumber :
     * isOpenLive : 0
     * openNum : 4
     * allNum : 50
     * list : [{"id":"d6d40ebf20f74a96ab83147a90f8cc37","name":"新余支队","pid":"e23a1005fe7b45c5ade1d029d8e3ed06","pname":"江西总队","category":"03","roomNumber":"","isOpenLive":"0","openNum":2,"allNum":31,"list":[{"id":"2a7dcda3e3db45b7bffd8b9ef56bef66","name":"渝水区大队","pid":"d6d40ebf20f74a96ab83147a90f8cc37","pname":"新余支队","category":"05","roomNumber":"","isOpenLive":"0","openNum":0,"allNum":10,"list":[{"id":"241371a3d5874132b3dabd7925e0f9e1","name":"平安路中队","pid":"2a7dcda3e3db45b7bffd8b9ef56bef66","pname":"渝水区大队","category":"09","roomNumber":"","isOpenLive":"0","openNum":0,"allNum":5,"list":[{"id":"47a6d7cdb98a4095bb37ee30e4960d9b","name":"赵云","pid":"241371a3d5874132b3dabd7925e0f9e1","pname":"平安路中队","category":"99","isOpenLive":"0","openNum":0,"allNum":0}]}]}]}]
     */

    private String id;
    private String name;
    private String pid;
    private String pname;
    private String category;
    private String roomNumber;
    private String isOpenLive;
    private int openNum;
    private int allNum;
    private List<ListBeanXXX> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getIsOpenLive() {
        return isOpenLive;
    }

    public void setIsOpenLive(String isOpenLive) {
        this.isOpenLive = isOpenLive;
    }

    public int getOpenNum() {
        return openNum;
    }

    public void setOpenNum(int openNum) {
        this.openNum = openNum;
    }

    public int getAllNum() {
        return allNum;
    }

    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }

    public List<ListBeanXXX> getList() {
        return list;
    }

    public void setList(List<ListBeanXXX> list) {
        this.list = list;
    }

    public static class ListBeanXXX {
        /**
         * id : d6d40ebf20f74a96ab83147a90f8cc37
         * name : 新余支队
         * pid : e23a1005fe7b45c5ade1d029d8e3ed06
         * pname : 江西总队
         * category : 03
         * roomNumber :
         * isOpenLive : 0
         * openNum : 2
         * allNum : 31
         * list : [{"id":"2a7dcda3e3db45b7bffd8b9ef56bef66","name":"渝水区大队","pid":"d6d40ebf20f74a96ab83147a90f8cc37","pname":"新余支队","category":"05","roomNumber":"","isOpenLive":"0","openNum":0,"allNum":10,"list":[{"id":"241371a3d5874132b3dabd7925e0f9e1","name":"平安路中队","pid":"2a7dcda3e3db45b7bffd8b9ef56bef66","pname":"渝水区大队","category":"09","roomNumber":"","isOpenLive":"0","openNum":0,"allNum":5,"list":[{"id":"47a6d7cdb98a4095bb37ee30e4960d9b","name":"赵云","pid":"241371a3d5874132b3dabd7925e0f9e1","pname":"平安路中队","category":"99","isOpenLive":"0","openNum":0,"allNum":0}]}]}]
         */

        private String id;
        private String name;
        private String pid;
        private String pname;
        private String category;
        private String roomNumber;
        private String isOpenLive;
        private int openNum;
        private int allNum;
        private List<ListBeanXX> list;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getRoomNumber() {
            return roomNumber;
        }

        public void setRoomNumber(String roomNumber) {
            this.roomNumber = roomNumber;
        }

        public String getIsOpenLive() {
            return isOpenLive;
        }

        public void setIsOpenLive(String isOpenLive) {
            this.isOpenLive = isOpenLive;
        }

        public int getOpenNum() {
            return openNum;
        }

        public void setOpenNum(int openNum) {
            this.openNum = openNum;
        }

        public int getAllNum() {
            return allNum;
        }

        public void setAllNum(int allNum) {
            this.allNum = allNum;
        }

        public List<ListBeanXX> getList() {
            return list;
        }

        public void setList(List<ListBeanXX> list) {
            this.list = list;
        }

        public static class ListBeanXX {
            /**
             * id : 2a7dcda3e3db45b7bffd8b9ef56bef66
             * name : 渝水区大队
             * pid : d6d40ebf20f74a96ab83147a90f8cc37
             * pname : 新余支队
             * category : 05
             * roomNumber :
             * isOpenLive : 0
             * openNum : 0
             * allNum : 10
             * list : [{"id":"241371a3d5874132b3dabd7925e0f9e1","name":"平安路中队","pid":"2a7dcda3e3db45b7bffd8b9ef56bef66","pname":"渝水区大队","category":"09","roomNumber":"","isOpenLive":"0","openNum":0,"allNum":5,"list":[{"id":"47a6d7cdb98a4095bb37ee30e4960d9b","name":"赵云","pid":"241371a3d5874132b3dabd7925e0f9e1","pname":"平安路中队","category":"99","isOpenLive":"0","openNum":0,"allNum":0}]}]
             */

            private String id;
            private String name;
            private String pid;
            private String pname;
            private String category;
            private String roomNumber;
            private String isOpenLive;
            private int openNum;
            private int allNum;
            private List<ListBeanX> list;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public String getPname() {
                return pname;
            }

            public void setPname(String pname) {
                this.pname = pname;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getRoomNumber() {
                return roomNumber;
            }

            public void setRoomNumber(String roomNumber) {
                this.roomNumber = roomNumber;
            }

            public String getIsOpenLive() {
                return isOpenLive;
            }

            public void setIsOpenLive(String isOpenLive) {
                this.isOpenLive = isOpenLive;
            }

            public int getOpenNum() {
                return openNum;
            }

            public void setOpenNum(int openNum) {
                this.openNum = openNum;
            }

            public int getAllNum() {
                return allNum;
            }

            public void setAllNum(int allNum) {
                this.allNum = allNum;
            }

            public List<ListBeanX> getList() {
                return list;
            }

            public void setList(List<ListBeanX> list) {
                this.list = list;
            }

            public static class ListBeanX {
                /**
                 * id : 241371a3d5874132b3dabd7925e0f9e1
                 * name : 平安路中队
                 * pid : 2a7dcda3e3db45b7bffd8b9ef56bef66
                 * pname : 渝水区大队
                 * category : 09
                 * roomNumber :
                 * isOpenLive : 0
                 * openNum : 0
                 * allNum : 5
                 * list : [{"id":"47a6d7cdb98a4095bb37ee30e4960d9b","name":"赵云","pid":"241371a3d5874132b3dabd7925e0f9e1","pname":"平安路中队","category":"99","isOpenLive":"0","openNum":0,"allNum":0}]
                 */

                private String id;
                private String name;
                private String pid;
                private String pname;
                private String category;
                private String roomNumber;
                private String isOpenLive;
                private int openNum;
                private int allNum;
                private List<ListBean> list;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getPid() {
                    return pid;
                }

                public void setPid(String pid) {
                    this.pid = pid;
                }

                public String getPname() {
                    return pname;
                }

                public void setPname(String pname) {
                    this.pname = pname;
                }

                public String getCategory() {
                    return category;
                }

                public void setCategory(String category) {
                    this.category = category;
                }

                public String getRoomNumber() {
                    return roomNumber;
                }

                public void setRoomNumber(String roomNumber) {
                    this.roomNumber = roomNumber;
                }

                public String getIsOpenLive() {
                    return isOpenLive;
                }

                public void setIsOpenLive(String isOpenLive) {
                    this.isOpenLive = isOpenLive;
                }

                public int getOpenNum() {
                    return openNum;
                }

                public void setOpenNum(int openNum) {
                    this.openNum = openNum;
                }

                public int getAllNum() {
                    return allNum;
                }

                public void setAllNum(int allNum) {
                    this.allNum = allNum;
                }

                public List<ListBean> getList() {
                    return list;
                }

                public void setList(List<ListBean> list) {
                    this.list = list;
                }

                public static class ListBean {
                    /**
                     * id : 47a6d7cdb98a4095bb37ee30e4960d9b
                     * name : 赵云
                     * pid : 241371a3d5874132b3dabd7925e0f9e1
                     * pname : 平安路中队
                     * category : 99
                     * isOpenLive : 0
                     * openNum : 0
                     * allNum : 0
                     */

                    private String id;
                    private String name;
                    private String pid;
                    private String pname;
                    private String category;
                    private String roomNumber;
                    private String isOpenLive;
                    private int openNum;
                    private int allNum;

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getPid() {
                        return pid;
                    }

                    public void setPid(String pid) {
                        this.pid = pid;
                    }

                    public String getPname() {
                        return pname;
                    }

                    public void setPname(String pname) {
                        this.pname = pname;
                    }

                    public String getCategory() {
                        return category;
                    }

                    public void setCategory(String category) {
                        this.category = category;
                    }

                    public String getRoomNumber() {
                        return roomNumber;
                    }

                    public void setRoomNumber(String roomNumber) {
                        this.roomNumber = roomNumber;
                    }

                    public String getIsOpenLive() {
                        return isOpenLive;
                    }

                    public void setIsOpenLive(String isOpenLive) {
                        this.isOpenLive = isOpenLive;
                    }

                    public int getOpenNum() {
                        return openNum;
                    }

                    public void setOpenNum(int openNum) {
                        this.openNum = openNum;
                    }

                    public int getAllNum() {
                        return allNum;
                    }

                    public void setAllNum(int allNum) {
                        this.allNum = allNum;
                    }
                }
            }
        }
    }
}
