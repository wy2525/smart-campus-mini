import { get, put, del, post } from "@/utils/request";
import type { ApiResponse, Comment, PageRequest, PageData } from "@/types";

/**
 * 获取评论列表
 */
export function getCommentList(params: PageRequest) {
  return get<PageData<Comment>>("/admin/comments", { params });
}

/**
 * 获取评论详情
 */
export function getCommentDetail(id: number) {
  return get<Comment>(`/admin/comments/${id}`);
}

/**
 * 更新评论状态
 */
export function updateCommentStatus(id: number, status: number) {
  return put<Comment>(`/admin/comments/${id}/status`, null, {
    params: { status },
  });
}

/**
 * 删除评论
 */
export function deleteComment(id: number) {
  return del<void>(`/admin/comments/${id}`);
}

/**
 * 批量删除评论
 */
export function batchDeleteComments(ids: number[]) {
  return del<void>("/admin/comments/batch", { data: ids });
}
