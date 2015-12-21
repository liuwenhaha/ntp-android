package com.ntp.model.gson;

import java.util.List;

/**
 * 作业消息
 * Created by lishuangxiang on 2015/12/21.
 */
public class HomeworkNoticeGson {

    /**
     * currentPage : 1
     * scores : [{"exercise":{"course":{"name":"C语言"},"name":"C语言第二章作业","time":"2015-05-23T00:00:00"},"id":2},{"exercise":{"course":{"name":"C语言"},"name":"C语言第三章作业","time":"2015-05-23T00:00:00"},"id":3},{"exercise":{"course":{"name":"C语言"},"name":"C语言第一章作业","time":"2015-05-02T00:00:00"},"id":1}]
     */

    private int currentPage;
    /**
     * exercise : {"course":{"name":"C语言"},"name":"C语言第二章作业","time":"2015-05-23T00:00:00"}
     * id : 2
     */

    private List<ScoresEntity> scores;

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setScores(List<ScoresEntity> scores) {
        this.scores = scores;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public List<ScoresEntity> getScores() {
        return scores;
    }

    public static class ScoresEntity {
        /**
         * course : {"name":"C语言"}
         * name : C语言第二章作业
         * time : 2015-05-23T00:00:00
         */

        private ExerciseEntity exercise;
        private int id;

        public void setExercise(ExerciseEntity exercise) {
            this.exercise = exercise;
        }

        public void setId(int id) {
            this.id = id;
        }

        public ExerciseEntity getExercise() {
            return exercise;
        }

        public int getId() {
            return id;
        }

        public static class ExerciseEntity {
            /**
             * name : C语言
             */

            private CourseEntity course;
            private String name;
            private String time;

            public void setCourse(CourseEntity course) {
                this.course = course;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public CourseEntity getCourse() {
                return course;
            }

            public String getName() {
                return name;
            }

            public String getTime() {
                return time;
            }

            public static class CourseEntity {
                private String name;

                public void setName(String name) {
                    this.name = name;
                }

                public String getName() {
                    return name;
                }
            }
        }
    }
}
