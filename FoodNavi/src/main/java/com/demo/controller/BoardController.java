package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.demo.domain.Board;
import com.demo.domain.Users;
import com.demo.dto.BoardScanVo;
import com.demo.dto.UserVo;
import com.demo.service.BoardCommentsService;
import com.demo.service.BoardService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class BoardController {

    @Autowired
    BoardService boardService;

    @Autowired
    BoardCommentsService boardCommentService;


    //게시글 작성으로 이동
    @GetMapping("/board_insert_form")
    public String showWriteForm(HttpSession session, Model model) {
        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");
        UserVo userVo = new UserVo();
        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/user_login_form");
            return "board/board_alert"; 
        } else {
            model.addAttribute("userVo", userVo);
            return "board/boardInsert"; //게시글 작성페이지로 이동.
        }

    }

    // 게시글 작성
    @PostMapping("/board_insert")
    public String saveBoard(@RequestParam("title") String title,
                            @RequestParam("content") String content,
                            HttpSession session,
                            HttpServletRequest request,
                            Model model) {

        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");
        UserVo userVo = new UserVo();
        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
        	 // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/user_login_form");
            return "board/board_alert"; 
        }

        Board vo = new Board();
        
        if (title.isEmpty()) {
        	vo.setTitle("제목 없음");
        }else {
        	vo.setTitle(title);
        }
        vo.setContent(content);
        vo.setUser(user); // 사용자 정보 설정
        model.addAttribute("userVo", userVo);
        boardService.insertBoard(vo);
        
        return "redirect:/board_list"; // 저장 후 리스트 페이지로 리다이렉트합니다.
    }


    // 게시글 리스트 보기
    @GetMapping("/board_list")
    public String showBoardList(Model model,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "5") int size,
                                @RequestParam(value = "sortBy", defaultValue = "bseq") String sortBy,
                                @RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection,
                                @RequestParam(value = "pageMaxDisplay", defaultValue = "5") int pageMaxDisplay,
                                @RequestParam(value = "searchField", defaultValue = "") String searchField,
                                @RequestParam(value = "searchWord", defaultValue = "") String searchWord,
                                BoardScanVo boardScanVo,
                                HttpSession session) {

        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");
        UserVo userVo = new UserVo(user);
        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
        	 // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/user_login_form");
            return "board/board_alert"; 
        }

        if (page == 0) {
            page = 1;
            boardScanVo = new BoardScanVo(); // 새로운 객체로 초기화
            boardScanVo.setSearchField(searchField);
            boardScanVo.setSearchWord(searchWord);
            boardScanVo.setSortBy(sortBy);
            boardScanVo.setSortDirection(sortDirection);
            boardScanVo.setPageMaxDisplay(pageMaxDisplay);

        } else {
            boardScanVo = (BoardScanVo) session.getAttribute("boardScanVo");
        }
        Page<Board> boardData = boardService.findBoardList(boardScanVo, page, size);

        boardScanVo.setPageInfo(boardData);
        boardScanVo.setBoardList(boardData.getContent());

            session.setAttribute("boardScanVo", boardScanVo);
            model.addAttribute("boardScanVo", boardScanVo);
            model.addAttribute("boardList", boardScanVo.getBoardList());
            model.addAttribute("pageInfo", boardScanVo.getPageInfo());
            model.addAttribute("boardBestList", boardService.getBestBoardList());
            model.addAttribute("userVo", userVo);
            return "board/boardList";
        }


    // 게시글 검색 보기
    @GetMapping("/board_list_search")
    public String searchBoardList(Model model,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "5") int size,
                                  @RequestParam(value = "sortBy", defaultValue = "bseq") String sortBy,
                                  @RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection,
                                  @RequestParam(value = "pageMaxDisplay", defaultValue = "5") int pageMaxDisplay,
                                  @RequestParam(value = "searchField", defaultValue = "") String searchField,
                                  @RequestParam(value = "searchWord", defaultValue = "") String searchWord,
                                  BoardScanVo boardScanVo,
                                  HttpSession session, HttpServletRequest request) {

        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");
        UserVo userVo = new UserVo();
        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/user_login_form");
            return "board/board_alert";
        }

        if (page == 0) {
            page = 1;
            boardScanVo = new BoardScanVo(); // 새로운 객체로 초기화
            boardScanVo.setSearchField(searchField);
            boardScanVo.setSearchWord(searchWord);
            boardScanVo.setSortBy(sortBy);
            boardScanVo.setSortDirection(sortDirection);
            boardScanVo.setPageMaxDisplay(pageMaxDisplay);


        } else {
            boardScanVo = (BoardScanVo) session.getAttribute("boardScanVo");

        }

        Page<Board> boardData = boardService.findBoardList(boardScanVo, page, size);

        if (boardData.isEmpty()) {
            model.addAttribute("msg", "검색 결과가 없습니다.");
            model.addAttribute("redirectTo", "/board_list");
            return "board/board_alert";
        } else {

            boardScanVo.setPageInfo(boardData);
            boardScanVo.setBoardList(boardData.getContent());
            session.setAttribute("boardScanVo", boardScanVo);
            model.addAttribute("boardScanVo", boardScanVo);
            model.addAttribute("boardList", boardScanVo.getBoardList());
            model.addAttribute("pageInfo", boardScanVo.getPageInfo());
            model.addAttribute("boardBestList", boardService.getBestBoardList());
            model.addAttribute("userVo", userVo);

            return "board/boardList";
        }
    }



    // 게시글 상세보기
    @GetMapping("/board_detail/{bseq}")
    public String boardDetail(@PathVariable("bseq") int bseq, Model model, HttpSession session, HttpServletRequest request) {

        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");
        UserVo userVo = new UserVo();
        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
        	 // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/user_login_form");
            return "board/board_alert"; 
        }

        // 게시글 번호를 통해 해당 게시글 가져오기
        Board board = boardService.getBoard(bseq);
        boardService.updateCnt(bseq);
        int useq = board.getUser().getUseq();
        // 모델에 게시글 추가
        model.addAttribute("board", board);
        model.addAttribute("authorList", boardService.getAuthorBoardList(useq));

        // 게시글의 작성자와 현재 사용자가 같은지 확인하여 모델에 추가
        model.addAttribute("isAuthor", board.getUser().getUseq() == user.getUseq());

        BoardScanVo boardScanVo = (BoardScanVo) session.getAttribute("boardScanVo");
        model.addAttribute("boardScanVo", boardScanVo);
        model.addAttribute("boardList", boardScanVo.getBoardList());
        model.addAttribute("pageInfo", boardScanVo.getPageInfo());
        model.addAttribute("userVo", userVo);
        // 게시글 상세보기 페이지로 이동
        return "board/boardDetail";
    }


    // 게시글 삭제하기
    @PostMapping("/board_delete/{bseq}")
    public String boardDelete(@PathVariable("bseq") int bseq, HttpSession session, HttpServletRequest request,
    		Model model) {

        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");
        UserVo userVo = new UserVo();
        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
        	 // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/user_login_form");
            return "board/board_alert"; 
        }
        model.addAttribute("userVo", userVo);
        boardCommentService.deletAllComment(bseq);
        boardService.deleteBoard(bseq);

        return "redirect:/board_list";

    }


    // 게시글 수정화면으로 이동하기
    @GetMapping("/board_edit_form/{bseq}")
    public String boardEditGo(@PathVariable("bseq") int bseq, Model model, HttpSession session, HttpServletRequest request) {

        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");
        UserVo userVo = new UserVo();
        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
        	 // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/user_login_form");
            return "board/board_alert"; 
        }

        // 게시글 번호를 통해 해당 게시글 가져오기
        Board board = boardService.getBoard(bseq);
        // 모델에 게시글 추가
        model.addAttribute("userVo", userVo);
        model.addAttribute("board", board);
        // 게시글 수정화면으로 이동
        return "board/boardEdit";
    }

    //게시글 수정하기
    @PostMapping("/board_edit")
    public String boardEdit(@RequestParam("title") String title,
                            @RequestParam("content") String content,
                            @RequestParam("bseq") int bseq,
                            @RequestParam("likes") int likes,
                            @RequestParam("cnt") int cnt,
                            HttpSession session, HttpServletRequest request, Model model) {

        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");
        UserVo userVo = new UserVo();
        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
        	 // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg","로그인 후 이용해주세요.");
            model.addAttribute("redirectTo","/user_login_form");
            return "board/board_alert"; 
        }

        Board vo = new Board();
        vo.setBseq(bseq);
        vo.setTitle(title);
        vo.setContent(content);
        vo.setUser(user);
        vo.setLikes(likes);
        vo.setCnt(cnt);
        model.addAttribute("userVo", userVo);

        boardService.editBoard(vo);
        boardCommentService.updateCommentCount(bseq);
        return "redirect:/board_list"; // 저장 후 리스트 페이지로 리다이렉트합니다.
    }

    @GetMapping("/board_userList/{useq}")
    public String showBoardList(Model model,
                                @PathVariable(value = "useq") int useq,
                                HttpSession session) {

        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");
        UserVo userVo = new UserVo();
        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg", "로그인 후 이용해주세요.");
            model.addAttribute("redirectTo", "/user_login_form");
            return "board/board_alert";
        }

            model.addAttribute("authorList", boardService.getAuthorBoardList(useq));
            model.addAttribute("commentList", boardCommentService.getCommentUserList(useq));
            model.addAttribute("userVo", userVo);
            return "board/boardUserList";
        }


    @PostMapping("board_like/{bseq}")
    @ResponseBody
    public ResponseEntity<String> likePost(@PathVariable("bseq") int bseq) {
        boardService.likePost(bseq);
        return ResponseEntity.ok("Liked");
    }

    @PostMapping("board_unlike/{bseq}")
    @ResponseBody
    public ResponseEntity<String> unlikePost(@PathVariable("bseq") int bseq){
        boardService.unlikePost(bseq);
        return ResponseEntity.ok("Liked");
    }
}
